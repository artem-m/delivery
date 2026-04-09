package microarch.delivery.adapters.in.http;

import lombok.RequiredArgsConstructor;
import microarch.basket.adapters.in.http.api.GetOrdersApi;
import microarch.basket.adapters.in.http.model.Location;
import microarch.basket.adapters.in.http.model.Order;
import microarch.delivery.core.application.queries.GetNotCompletedOrdersCommand;
import microarch.delivery.core.application.queries.GetNotCompletedOrdersHandler;
import microarch.delivery.core.application.queries.GetNotCompletedOrdersResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GetOrdersController implements GetOrdersApi {
    private final GetNotCompletedOrdersHandler handler;

    @Override
    public ResponseEntity<List<Order>> getOrders() {
        var result = handler.apply(new GetNotCompletedOrdersCommand());
        if (result.isFailure()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(result.getValue().items().stream().map(this::toOrder).toList());
    }

    private Order toOrder(GetNotCompletedOrdersResponse.Item it) {
        return new Order(it.id(), new Location(it.location().getX(), it.location().getY()));
    }
}
