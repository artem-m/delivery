package microarch.delivery.core.application.commands;

import libs.ddd.DomainEventPublisher;
import libs.errs.Error;
import libs.errs.Guard;
import libs.errs.UnitResult;
import lombok.RequiredArgsConstructor;
import microarch.delivery.core.ports.CourierRepository;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CompleteOrderHandler implements Function<CompleteOrderCommand, UnitResult<Error>> {
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final DomainEventPublisher domainEventPublisher;

    @Override
    @Transactional
    public UnitResult<Error> apply(CompleteOrderCommand command) {
        var order = orderRepository.get(command.orderId()).orElse(null);
        var error = Guard.againstNull(order, "Order by id");
        if (error != null) {
            return UnitResult.failure(error);
        }
        var courier = courierRepository.get(command.courierId()).orElse(null);
        error = Guard.againstNull(courier, "Courier by id");
        if (error != null) {
            return UnitResult.failure(error);
        }
        var result = courier.complete(order);
        if (result.isFailure()) {
            return UnitResult.failure(result.getError());
        }
        result = order.complete();
        if (result.isFailure()) {
            return UnitResult.failure(result.getError());
        }
        orderRepository.update(order);
        courierRepository.update(courier);
        domainEventPublisher.publish(List.of(order, courier));
        return UnitResult.success();
    }
}
