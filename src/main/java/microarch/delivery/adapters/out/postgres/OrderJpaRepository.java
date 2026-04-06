package microarch.delivery.adapters.out.postgres;

import microarch.delivery.core.domain.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findTopByStatus(Order.OrderStatus status);

    Collection<Order> findAllByStatus(Order.OrderStatus status);

    Collection<Order> findAllByStatusNot(Order.OrderStatus status);
}
