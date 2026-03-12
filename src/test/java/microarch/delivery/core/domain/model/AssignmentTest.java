package microarch.delivery.core.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AssignmentTest {

    @Test
    public void shouldCreateValidAssignment() {
        var valid = Assignment.create(UUID.randomUUID(), Volume.create(3), Location.create(2, 2));
        assertThat(valid).isNotNull();
        assertThat(valid.isFailure()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("invalidAssignmentParams")
    public void shouldNotCreateInvalidAssignment(UUID orderId, Volume volume, Location location) {
        var invalid = Assignment.create(orderId, volume, location);
        assertThat(invalid).isNotNull();
        assertThat(invalid.isFailure()).isTrue();
    }

    @Test
    public void shouldBelongToOrderCreatedFor() {
        var order = Order.create(Location.create(3, 3), Volume.create(3)).getValue();
        var assignment = Assignment.createFor(order).getValue();
        assertThat(assignment.isBelongsTo(order)).isTrue();
    }

    @Test
    public void shouldNotBelongToOrderNotCreatedFor() {
        var assignment = Assignment.createFor(Order.create(Location.create(3, 3), Volume.create(3)).getValue())
                .getValue();
        assertThat(assignment.isBelongsTo(Order.create(Location.create(3, 3), Volume.create(3)).getValue())).isFalse();
    }

    public static Stream<Arguments> invalidAssignmentParams() {
        return Stream.of(Arguments.of(null, null, null), Arguments.of(UUID.randomUUID(), null, null),
                Arguments.of(UUID.randomUUID(), Volume.create(3), null));
    }

}