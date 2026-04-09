package microarch.delivery.core.domain.model.order;

import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.Volume;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    public void shouldCreateValidOrder() {
        var result = Order.create(UUID.randomUUID(), Location.create(3, 3), Volume.create(3));
        assertThat(result.isFailure()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("invalidCreationParams")
    public void shouldNotCreateInvalidOrder() {
        var result = Order.create(UUID.randomUUID(), Location.create(3, 3), Volume.create(3));
        assertThat(result.isFailure()).isFalse();
    }

    @Test
    public void shouldAssignCreatedOrder() {
        var order = Order.create(UUID.randomUUID(), Location.create(3, 3), Volume.create(3)).getValue();
        var result = order.assign();
        assertThat(result.isFailure()).isFalse();
    }

    @Test
    public void shouldNotAssignOrderTwice() {
        var order = Order.create(UUID.randomUUID(), Location.create(3, 3), Volume.create(3)).getValue();
        var result = order.assign();
        assertThat(result.isFailure()).isFalse();
        var result2 = order.assign();
        assertThat(result2.isFailure()).isTrue();
    }

    @Test
    public void shouldCompleteAssignedOrder() {
        var order = Order.create(UUID.randomUUID(), Location.create(3, 3), Volume.create(3)).getValue();
        var result = order.assign();
        assertThat(result.isFailure()).isFalse();
        var result2 = order.complete();
        assertThat(result2.isFailure()).isFalse();
    }

    @Test
    public void shouldNotCompleteCompletedOrder() {
        var order = Order.create(UUID.randomUUID(), Location.create(3, 3), Volume.create(3)).getValue();
        var result = order.assign();
        assertThat(result.isFailure()).isFalse();
        var result2 = order.complete();
        assertThat(result2.isFailure()).isFalse();
        var result3 = order.complete();
        assertThat(result3.isFailure()).isTrue();
    }

    @Test
    public void shouldNotCompleteCreatedOrder() {
        var order = Order.create(UUID.randomUUID(), Location.create(3, 3), Volume.create(3)).getValue();
        var result3 = order.complete();
        assertThat(result3.isFailure()).isTrue();
    }

    public static Stream<Arguments> invalidCreationParams() {
        return Stream.of(Arguments.of(null, Location.create(3, 3), Volume.create(3)),
                Arguments.of(UUID.randomUUID(), null, Volume.create(3)),
                Arguments.of(UUID.randomUUID(), Location.create(3, 3), null));
    }
}