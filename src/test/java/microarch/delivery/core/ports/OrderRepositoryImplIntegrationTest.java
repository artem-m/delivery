package microarch.delivery.core.ports;

import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.Volume;
import microarch.delivery.core.domain.model.order.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryImplIntegrationTest extends BasePgIntegrationTest {

    @Autowired
    OrderRepository repository;

    @Test
    public void shouldCreateOrder() {
        var order = Order.create(UUID.randomUUID(), Location.create(2, 2), Volume.create(2)).getValue();
        var created = repository.create(order);
        assertThat(created).isNotNull().usingRecursiveComparison() // assert fields
                .isEqualTo(order);
    }

    @Test
    public void shouldUpdateOrder() {
        var order = Order.create(UUID.randomUUID(), Location.create(2, 2), Volume.create(2)).getValue();
        var created = repository.create(order);
        created.assign();
        var updated = repository.update(created);
        assertThat(updated).isNotNull().usingRecursiveComparison() // assert fields
                .isEqualTo(created);
    }

    @Test
    public void shouldFindAllAssigned() {
        var order = Order.create(UUID.randomUUID(), Location.create(2, 2), Volume.create(2)).getValue();
        var created = repository.create(order);
        created.assign();
        var updated = repository.update(created);
        var assigned = repository.findAllAssigned();
        assertThat(assigned).contains(updated);
    }

}