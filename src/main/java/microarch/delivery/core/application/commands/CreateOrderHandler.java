package microarch.delivery.core.application.commands;

import libs.errs.Error;
import libs.errs.UnitResult;
import lombok.RequiredArgsConstructor;
import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.Volume;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CreateOrderHandler implements Function<CreateOrderCommand, UnitResult<Error>> {
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public UnitResult<Error> apply(CreateOrderCommand command) {
        var result = Order.create(command.orderId(), Location.create(2, 2), Volume.create(command.volume()));
        if (result.isFailure()) {
            return UnitResult.failure(result.getError());
        }
        orderRepository.create(result.getValue());
        return UnitResult.success();
    }
}
