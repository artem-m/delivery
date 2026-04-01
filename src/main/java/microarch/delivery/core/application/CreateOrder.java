package microarch.delivery.core.application;

import lombok.RequiredArgsConstructor;
import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.Volume;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.domain.services.OrderDistributionService;
import microarch.delivery.core.ports.CourierRepository;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrder {
    private final OrderRepository orderRepository;
    private final CourierRepository courierRepository;
    private final OrderDistributionService orderDistributionService;

    @Transactional
    public void createOrderAndAssign(Location from, Volume volume, String courierName) {
        // create an order
        var result = Order.create(UUID.randomUUID(), from, volume);
        orderRepository.create(result.getValueOrThrow());
        // create a courier
        var courierResult = Courier.create(courierName, from);
        courierRepository.create(courierResult.getValueOrThrow());
        // assign
        var assignResult = orderDistributionService.assign(result.getValue(), List.of(courierResult.getValue()));
        var updatedCourier = assignResult.getValueOrThrow();
        courierRepository.update(updatedCourier);
    }
}
