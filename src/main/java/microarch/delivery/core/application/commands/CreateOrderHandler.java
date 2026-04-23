package microarch.delivery.core.application.commands;

import libs.ddd.DomainEventPublisher;
import libs.errs.Error;
import libs.errs.UnitResult;
import lombok.RequiredArgsConstructor;
import microarch.delivery.core.domain.model.Volume;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.ports.GeoServiceClient;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CreateOrderHandler implements Function<CreateOrderCommand, UnitResult<Error>> {
    private final OrderRepository orderRepository;
    private final GeoServiceClient geoServiceClient;
    private final DomainEventPublisher domainEventPublisher;

    @Override
    @Transactional
    public UnitResult<Error> apply(CreateOrderCommand command) {
        var location = geoServiceClient.getGeoLocation(command.address().getStreet());
        if (location.isFailure()) {
            return UnitResult.failure(location.getError());
        }
        var result = Order.create(command.orderId(), location.getValue(), Volume.create(command.volume()));
        if (result.isFailure()) {
            return UnitResult.failure(result.getError());
        }
        orderRepository.create(result.getValue());
        domainEventPublisher.publish(List.of(result.getValue()));
        return UnitResult.success();
    }
}
