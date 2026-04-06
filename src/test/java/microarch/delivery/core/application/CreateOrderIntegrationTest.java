package microarch.delivery.core.application;

import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.Volume;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.BasePgIntegrationTest;
import microarch.delivery.core.ports.CourierRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreateOrderIntegrationTest extends BasePgIntegrationTest {
    @Autowired
    CreateOrder createOrder;
    @Autowired
    CourierRepository courierRepository;

    @Test
    public void shouldCreateOrderInTransaction() {
        String bob = "bob-" + System.currentTimeMillis();
        createOrder.createOrderAndAssign(Location.create(3, 3), Volume.create(3), bob);
        var courier = courierRepository.findAll().stream().filter(it -> Objects.equals(bob, it.getName())).findFirst()
                .orElseThrow();
        assertThat(courier).isNotNull();
        assertThat(courier.getAssignments()).hasSize(1);
    }

    @Test
    public void shouldNoCreateOrderInFailedTransaction() {
        String bob = "bob-" + System.currentTimeMillis();
        var tooBigVolume = Courier.MAX_VOLUME.addTo(Volume.create(3)).getValue();
        assertThrows(libs.errs.DomainInvariantException.class,
                () -> createOrder.createOrderAndAssign(Location.create(3, 3), tooBigVolume, bob));
        var courier = courierRepository.findAll().stream().filter(it -> Objects.equals(bob, it.getName())).findFirst();
        assertThat(courier).isEmpty();
    }
}