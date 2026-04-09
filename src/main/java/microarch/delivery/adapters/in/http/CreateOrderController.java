package microarch.delivery.adapters.in.http;

import lombok.RequiredArgsConstructor;
import microarch.basket.adapters.in.http.api.CreateOrderApi;
import microarch.basket.adapters.in.http.model.CreateOrderResponse;
import microarch.delivery.core.application.commands.CreateOrderCommand;
import microarch.delivery.core.application.commands.CreateOrderHandler;
import microarch.delivery.core.domain.model.Address;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CreateOrderController implements CreateOrderApi {
    private final CreateOrderHandler handler;

    @Override
    public ResponseEntity<CreateOrderResponse> createOrder() {
        var result = handler.apply(new CreateOrderCommand(UUID.randomUUID(),
                Address.create("country1", "city1", "street1", "house1", "ap1"), 4));
        if (result.isFailure()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok().build();
    }
}
