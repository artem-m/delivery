package microarch.delivery.core.application.commands;

import libs.errs.Error;
import libs.errs.Guard;
import libs.errs.UnitResult;
import lombok.RequiredArgsConstructor;
import microarch.delivery.core.domain.services.OrderDistributionService;
import microarch.delivery.core.ports.CourierRepository;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class AssignOrderHandler implements Function<AssignOrderCommand, UnitResult<Error>> {
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final OrderDistributionService orderDistributionService;

    @Override
    @Transactional
    public UnitResult<Error> apply(AssignOrderCommand assignOrderCommand) {
        var order = orderRepository.getAnyCreated().orElse(null);
        var error = Guard.againstNull(order, "Any order");
        if (error != null) {
            return UnitResult.failure(error);
        }
        var assignResult = orderDistributionService.assign(order, courierRepository.findAll());
        var updatedCourier = assignResult.getValueOrThrow();
        courierRepository.update(updatedCourier);
        return UnitResult.success();
    }
}
