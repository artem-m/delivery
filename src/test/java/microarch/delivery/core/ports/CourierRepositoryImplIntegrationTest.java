package microarch.delivery.core.ports;

import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.Volume;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.order.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CourierRepositoryImplIntegrationTest extends BasePgIntegrationTest {

    @Autowired
    CourierRepository repository;

    @Test
    public void shouldCreateOrder() {
        var entity = Courier.create("name", Location.create(3, 3)).getValue();
        var created = repository.create(entity);
        assertThat(created).isNotNull().usingRecursiveComparison() // assert fields
                .isEqualTo(entity);
    }

    @Test
    public void shouldUpdateOrder() {
        var entity = Courier.create("name", Location.create(3, 3)).getValue();
        var created = repository.create(entity);
        created.assign(Order.create(UUID.randomUUID(), Location.create(3, 3), Volume.create(3)).getValue());
        var updated = repository.update(created);
        assertThat(updated).isNotNull().usingRecursiveComparison() // assert fields
                .isEqualTo(created);
    }

    @Test
    public void shouldFindAllAssigned() {
        var entity = Courier.create("name", Location.create(3, 3)).getValue();
        var created = repository.create(entity);
        var all = repository.findAll();
        assertThat(all).contains(created);
    }

}