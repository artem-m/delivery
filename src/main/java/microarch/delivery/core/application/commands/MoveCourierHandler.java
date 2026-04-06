package microarch.delivery.core.application.commands;

import libs.errs.Error;
import libs.errs.Guard;
import libs.errs.UnitResult;
import lombok.RequiredArgsConstructor;
import microarch.delivery.core.ports.CourierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MoveCourierHandler implements Function<MoveCourierCommand, UnitResult<Error>> {
    private final CourierRepository courierRepository;

    @Override
    @Transactional
    public UnitResult<Error> apply(MoveCourierCommand command) {
        var courier = courierRepository.get(command.courierId()).orElse(null);
        var error = Guard.againstNull(courier, "Courier by id");
        if (error != null) {
            return UnitResult.failure(error);
        }
        var result = courier.moveTo(command.location());
        if (result.isFailure()) {
            return UnitResult.failure(result.getError());
        }
        courierRepository.update(courier);
        return UnitResult.success();
    }
}
