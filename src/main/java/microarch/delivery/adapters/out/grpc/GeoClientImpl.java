package microarch.delivery.adapters.out.grpc;

import clients.geo.GeoGrpc;
import clients.geo.GeoProto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import libs.errs.Error;
import libs.errs.Result;
import lombok.extern.slf4j.Slf4j;
import microarch.delivery.ApplicationProperties;
import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.ports.GeoServiceClient;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Slf4j
@Service
public class GeoClientImpl implements GeoServiceClient {

    private final ManagedChannel channel;
    private final GeoGrpc.GeoBlockingStub stub;

    public GeoClientImpl(ApplicationProperties properties) {
        this.channel = ManagedChannelBuilder.forAddress(properties.getGrpc().getGeoService().getHost(),
                properties.getGrpc().getGeoService().getPort()).usePlaintext().build();
        this.stub = GeoGrpc.newBlockingStub(channel);
    }

    @PreDestroy
    public void stop() {
        if (!channel.isShutdown()) {
            channel.shutdown();
        }
    }

    @Override
    public Result<Location, Error> getGeoLocation(String street) {
        GeoProto.Location location = null;
        try {
            var response = stub.getGeolocation(GeoProto.GetGeolocationRequest.newBuilder().setStreet(street).build());
            if (!response.hasLocation()) {
                return Result.failure(Error.of("no.location.for.street", "No location found for street=" + street));
            }
            location = response.getLocation();
        } catch (Exception e) {
            return Result.failure(
                    Error.of("failed.to.get.location.for.street", "Failed to get location for street=" + street));
        }
        return Result.success(Location.create(location.getX(), location.getY()));
    }
}
