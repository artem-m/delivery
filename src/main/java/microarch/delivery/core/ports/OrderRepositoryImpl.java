package microarch.delivery.core.ports;

import lombok.RequiredArgsConstructor;
import microarch.delivery.adapters.out.postgres.OrderJpaRepository;
import microarch.delivery.core.domain.model.order.Order;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order create(Order entity) {
        return orderJpaRepository.save(entity);
    }

    @Override
    public Order update(Order entity) {
        return orderJpaRepository.save(entity);
    }

    @Override
    public Optional<Order> get(UUID id) {
        return orderJpaRepository.findById(id);
    }

    @Override
    public Optional<Order> getAnyCreated() {
        return orderJpaRepository.findTopByStatus(Order.OrderStatus.Created);
    }

    @Override
    public Collection<Order> findAllAssigned() {
        return orderJpaRepository.findAllByStatus(Order.OrderStatus.Assigned);
    }
}
