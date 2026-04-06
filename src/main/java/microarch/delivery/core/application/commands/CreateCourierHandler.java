package microarch.delivery.core.application.commands;

import libs.errs.Error;
import libs.errs.UnitResult;
import lombok.RequiredArgsConstructor;
import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.ports.CourierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CreateCourierHandler implements Function<CreateCourierCommand, UnitResult<Error>> {
    private final CourierRepository courierRepository;

    @Override
    @Transactional
    public UnitResult<Error> apply(CreateCourierCommand command) {
        var courierResult = Courier.create(command.name(), Location.create(3, 3));
        courierRepository.create(courierResult.getValueOrThrow());
        return UnitResult.success();
    }
}
