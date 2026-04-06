package microarch.delivery.core.domain.model.courier;

import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.Volume;
import microarch.delivery.core.domain.model.order.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CourierTest {

    @Test
    public void shouldCreateValidCourier() {
        var courier = Courier.create("name", Location.create(2, 2));
        assertThat(courier.getValue()).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("invalidCourierParams")
    public void shouldNotCreateInvalidCourier(String name, Location loc) {
        var courier = Courier.create(name, loc);
        assertThat(courier.isFailure()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("locationsToMoveFrom22")
    public void shouldMoveToValidLocationsOnly(Location loc, boolean isFailure) {
        var courier = Courier.create("name", Location.create(2, 2)).getValue();
        var result = courier.moveTo(loc);
        assertThat(result.isFailure()).isEqualTo(isFailure);
    }

    @Test
    public void shouldNotAssignBigOrder() {
        var courier = Courier.create("name", Location.create(2, 2)).getValue();
        var order = Order
                .create(UUID.randomUUID(), Location.create(3, 3), Courier.MAX_VOLUME.addTo(Volume.create(3)).getValue())
                .getValue();
        var result = courier.assign(order);
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    public void shouldNotAssignOrderTwice() {
        var courier = Courier.create("name", Location.create(2, 2)).getValue();
        var order = Order.create(UUID.randomUUID(), Location.create(3, 3), Volume.create(3)).getValue();
        var result = courier.assign(order);
        assertThat(result.isFailure()).isFalse();
        var result2 = courier.assign(order);
        assertThat(result2.isFailure()).isTrue();
    }

    @Test
    public void shouldCompleteOrder() {
        var courier = Courier.create("name", Location.create(2, 2)).getValue();
        var order = Order.create(UUID.randomUUID(), Location.create(3, 3), Volume.create(3)).getValue();
        var result = courier.assign(order);
        assertThat(result.isFailure()).isFalse();
        var result2 = courier.complete(order);
        assertThat(result2.isFailure()).isFalse();
    }

    @Test
    public void shouldNotCompleteUnknownOrder() {
        var courier = Courier.create("name", Location.create(2, 2)).getValue();
        var order = Order.create(UUID.randomUUID(), Location.create(3, 3), Volume.create(3)).getValue();
        var result2 = courier.complete(order);
        assertThat(result2.isFailure()).isTrue();
    }

    public static Stream<Arguments> locationsToMoveFrom22() {
        return Stream.of(Arguments.of(Location.create(3, 2), false), Arguments.of(Location.create(3, 1), false),
                Arguments.of(Location.create(3, 3), false), Arguments.of(Location.create(2, 2), false),
                Arguments.of(Location.create(2, 1), false), Arguments.of(Location.create(2, 3), false),
                Arguments.of(Location.create(1, 2), false), Arguments.of(Location.create(1, 1), false),
                Arguments.of(Location.create(1, 3), false), Arguments.of(Location.create(4, 2), true),
                Arguments.of(Location.create(4, 1), true), Arguments.of(Location.create(4, 3), true),
                Arguments.of(Location.create(2, 4), true), Arguments.of(Location.create(1, 4), true),
                Arguments.of(Location.create(3, 4), true));
    }

    public static Stream<Arguments> invalidCourierParams() {
        return Stream.of(Arguments.of(null, null), Arguments.of("", null), Arguments.of(null, Location.create(2, 2)),
                Arguments.of("", Location.create(2, 2)), Arguments.of("name", null));
    }
}