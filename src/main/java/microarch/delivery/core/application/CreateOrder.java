package microarch.delivery.core.application;

import lombok.RequiredArgsConstructor;
import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.Volume;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.order.Order;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrder {
    private final OrderRepository orderRepository;

    @Transactional
    public void createOrderAndAssign(Location from, Volume volume) {
        var result = Order.create(UUID.randomUUID(), from, volume);
        orderRepository.create(result.getValueOrThrow());
    }
}
