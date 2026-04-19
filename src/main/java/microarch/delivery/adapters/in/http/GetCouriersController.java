package microarch.delivery.adapters.in.http;

import lombok.RequiredArgsConstructor;
import microarch.basket.adapters.in.http.api.GetCouriersApi;
import microarch.basket.adapters.in.http.model.Courier;
import microarch.basket.adapters.in.http.model.Location;
import microarch.delivery.core.application.queries.GetAllCouriersHandler;
import microarch.delivery.core.application.queries.GetAllCouriersQuery;
import microarch.delivery.core.application.queries.GetAllCouriersResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GetCouriersController implements GetCouriersApi {
    private final GetAllCouriersHandler handler;

    @Override
    public ResponseEntity<List<Courier>> getCouriers() {
        var result = handler.apply(new GetAllCouriersQuery());
        if (result.isFailure()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(result.getValue().items().stream().map(this::toCourier).toList());
    }

    private Courier toCourier(GetAllCouriersResponse.Item it) {
        return new Courier(it.id(), it.name(), new Location(it.location().getX(), it.location().getY()));
    }
}
