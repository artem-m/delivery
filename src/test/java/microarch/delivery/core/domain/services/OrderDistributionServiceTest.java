package microarch.delivery.core.domain.services;

import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.Volume;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.order.Order;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderDistributionServiceTest {
    OrderDistributionService service = new OrderDistributionServiceImpl();

    @Test
    public void shouldNotAssignNullCreated() {
        var result = service.assign(null, List.of(Courier.create("man", Location.create(3, 3)).getValue()));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    public void shouldNotAssignNotCreatedOrder() {
        Order order = Order.create(UUID.randomUUID(), Location.create(3, 4), Volume.create(2)).getValue();
        order.assign();
        var result = service.assign(order, List.of(Courier.create("man", Location.create(3, 3)).getValue()));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    public void shouldNotAssignOrderToFilledCourier() {
        Order order = Order.create(UUID.randomUUID(), Location.create(3, 4), Volume.create(29)).getValue();
        var result = service.assign(order, List.of(Courier.create("man", Location.create(3, 3)).getValue()));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    public void shouldNotAssignOrderToClosestCourier() {
        Order order = Order.create(UUID.randomUUID(), Location.create(3, 4), Volume.create(2)).getValue();
        Courier closest = Courier.create("man", Location.create(3, 3)).getValue();
        Courier furthest = Courier.create("man", Location.create(9, 9)).getValue();
        var result = service.assign(order, List.of(closest, furthest));
        assertThat(result.isFailure()).isFalse();
        assertThat(result.getValue()).isEqualTo(closest);
    }

    @Test
    public void shouldNotAssignOrderToEmptyListOfCouriers() {
        Order order = Order.create(UUID.randomUUID(), Location.create(3, 4), Volume.create(2)).getValue();
        var result = service.assign(order, List.of());
        assertThat(result.isFailure()).isTrue();
    }
}