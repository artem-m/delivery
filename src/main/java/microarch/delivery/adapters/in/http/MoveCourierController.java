package microarch.delivery.adapters.in.http;

import lombok.RequiredArgsConstructor;
import microarch.basket.adapters.in.http.api.MoveCourierApi;
import microarch.basket.adapters.in.http.model.Location;
import microarch.delivery.core.application.commands.MoveCourierCommand;
import microarch.delivery.core.application.commands.MoveCourierHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class MoveCourierController implements MoveCourierApi {
    private final MoveCourierHandler handler;

    @Override
    public ResponseEntity<Void> moveCourier(UUID courierId, Location location) {
        var result = handler.apply(new MoveCourierCommand(courierId,
                microarch.delivery.core.domain.model.Location.create(location.getX(), location.getY())));
        if (result.isFailure()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok().build();
    }
}
