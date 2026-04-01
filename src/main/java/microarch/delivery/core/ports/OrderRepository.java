package microarch.delivery.core.ports;

import microarch.delivery.core.domain.model.order.Order;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order create(Order entity);

    Order update(Order entity);

    Optional<Order> get(UUID id);

    Optional<Order> getAnyCreated();

    Collection<Order> findAllAssigned(); // offset, limit
}
