package microarch.delivery.adapters.out;

import clients.geo.GeoGrpc;
import clients.geo.GeoProto;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.ports.GeoServiceClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeoClientImpl implements GeoServiceClient {

    private final GeoGrpc.AsyncService grpcService;

    @Override
    public Location getGeoLocation(String street) {
        var ref = new AtomicReference<Location>();
        var latch = new CountDownLatch(1);
        StreamObserver<GeoProto.GetGeolocationReply> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(GeoProto.GetGeolocationReply value) {
                if (value.hasLocation()) {
                    var loc = value.getLocation();
                    var result = Location.create(loc.getX(), loc.getY());
                    ref.set(result);
                }
                latch.countDown();
            }

            @Override
            public void onError(Throwable t) {
            log.error("Error requesting location", t);
                latch.countDown();
            }

            @Override
            public void onCompleted() {
latch.countDown();
            }
        };
        grpcService.getGeolocation(
                GeoProto.GetGeolocationRequest.newBuilder().setStreet(street).build(),
                responseObserver);
        try {
            latch.wait(1_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        var result = ref.get();
        if (result==null){
            throw new IllegalStateException("Did not get any location for street="+street);
        }
        return result;
    }
}
