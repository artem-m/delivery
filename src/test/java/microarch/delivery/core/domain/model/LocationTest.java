package microarch.delivery.core.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static microarch.delivery.core.domain.model.Location.MAX_COORD_VALUE;
import static microarch.delivery.core.domain.model.Location.MIN_COORD_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    private static int CENTER_COORD = (MAX_COORD_VALUE + MIN_COORD_VALUE) / 2;

    @Test
    public void shouldCreateValidLocation() {
        var location = Location.create(MIN_COORD_VALUE, MIN_COORD_VALUE);
        assertThat(location).isNotNull().isEqualTo(Location.create(MIN_COORD_VALUE, MIN_COORD_VALUE));
    }

    @ParameterizedTest
    @MethodSource("invalidCoordinates")
    public void shouldNotCreateInvalidLocation(int x, int y) {
        assertThrows(IllegalArgumentException.class, () -> Location.create(x, y));
    }

    @Test
    public void shouldNotCalculateDistanceToNullLocation() {
        assertThrows(IllegalArgumentException.class,
                () -> Location.create(MIN_COORD_VALUE, MIN_COORD_VALUE).distanceTo(null));
    }

    @ParameterizedTest
    @MethodSource("validDistancesToCenter")
    public void shouldCalculateValidLocationTo(int x, int y, int distance) {
        var src = Location.create(CENTER_COORD, CENTER_COORD);
        var dst = Location.create(x, y);
        var srcToDst = src.distanceTo(dst);
        var dstToSrc = dst.distanceTo(src);
        assertThat(srcToDst).isEqualTo(dstToSrc).isEqualTo(distance);
    }

    public static Stream<Arguments> invalidCoordinates() {
        return Stream.of(Arguments.of(0, 0), Arguments.of(11, 11), Arguments.of(-MIN_COORD_VALUE, 0),
                Arguments.of(0, -MIN_COORD_VALUE), Arguments.of(Integer.MIN_VALUE, 0),
                Arguments.of(0, Integer.MIN_VALUE), Arguments.of(Integer.MAX_VALUE, 0),
                Arguments.of(0, Integer.MAX_VALUE), Arguments.of(11, 0), Arguments.of(0, 11),
                Arguments.of(Integer.MIN_VALUE, 5), Arguments.of(5, Integer.MIN_VALUE),
                Arguments.of(Integer.MAX_VALUE, 5), Arguments.of(5, Integer.MAX_VALUE), Arguments.of(11, 5),
                Arguments.of(5, 111111), Arguments.of(11, 5), Arguments.of(5, 111111));
    }

    public static Stream<Arguments> validDistancesToCenter() {
        return Stream.of(Arguments.of(MIN_COORD_VALUE, MIN_COORD_VALUE, 8), Arguments.of(MIN_COORD_VALUE, 2, 7),
                Arguments.of(MIN_COORD_VALUE, 3, 6), Arguments.of(MIN_COORD_VALUE, 4, 5),
                Arguments.of(MIN_COORD_VALUE, 5, 4), Arguments.of(MIN_COORD_VALUE, 6, 5),
                Arguments.of(MIN_COORD_VALUE, 7, 6), Arguments.of(MIN_COORD_VALUE, 8, 7),
                Arguments.of(MIN_COORD_VALUE, 9, 8), Arguments.of(2, MIN_COORD_VALUE, 7), Arguments.of(2, 2, 6),
                Arguments.of(2, 3, 5), Arguments.of(2, 4, 4), Arguments.of(2, 5, 3), Arguments.of(2, 6, 4),
                Arguments.of(2, 7, 5), Arguments.of(2, 8, 6), Arguments.of(2, 9, 7),
                Arguments.of(3, MIN_COORD_VALUE, 6), Arguments.of(3, 2, 5), Arguments.of(3, 3, 4),
                Arguments.of(3, 4, 3), Arguments.of(3, 5, 2), Arguments.of(3, 6, 3), Arguments.of(3, 7, 4),
                Arguments.of(3, 8, 5), Arguments.of(3, 9, 6), Arguments.of(4, MIN_COORD_VALUE, 5),
                Arguments.of(4, 2, 4), Arguments.of(4, 3, 3), Arguments.of(4, 4, 2),
                Arguments.of(4, 5, MIN_COORD_VALUE), Arguments.of(4, 6, 2), Arguments.of(4, 7, 3),
                Arguments.of(4, 8, 4), Arguments.of(4, 9, 5), Arguments.of(5, MIN_COORD_VALUE, 4),
                Arguments.of(5, 2, 3), Arguments.of(5, 3, 2), Arguments.of(5, 4, MIN_COORD_VALUE),
                Arguments.of(5, 5, 0), Arguments.of(5, 6, MIN_COORD_VALUE), Arguments.of(5, 7, 2),
                Arguments.of(5, 8, 3), Arguments.of(5, 9, 4), Arguments.of(6, MIN_COORD_VALUE, 5),
                Arguments.of(6, 2, 4), Arguments.of(6, 3, 3), Arguments.of(6, 4, 2),
                Arguments.of(6, 5, MIN_COORD_VALUE), Arguments.of(6, 6, 2), Arguments.of(6, 7, 3),
                Arguments.of(6, 8, 4), Arguments.of(6, 9, 5), Arguments.of(7, MIN_COORD_VALUE, 6),
                Arguments.of(7, 2, 5), Arguments.of(7, 3, 4), Arguments.of(7, 4, 3), Arguments.of(7, 5, 2),
                Arguments.of(7, 6, 3), Arguments.of(7, 7, 4), Arguments.of(7, 8, 5), Arguments.of(7, 9, 6),
                Arguments.of(8, MIN_COORD_VALUE, 7), Arguments.of(8, 2, 6), Arguments.of(8, 3, 5),
                Arguments.of(8, 4, 4), Arguments.of(8, 5, 3), Arguments.of(8, 6, 4), Arguments.of(8, 7, 5),
                Arguments.of(8, 8, 6), Arguments.of(8, 9, 7), Arguments.of(9, MIN_COORD_VALUE, 8),
                Arguments.of(9, 2, 7), Arguments.of(9, 3, 6), Arguments.of(9, 4, 5), Arguments.of(9, 5, 4),
                Arguments.of(9, 6, 5), Arguments.of(9, 7, 6), Arguments.of(9, 8, 7), Arguments.of(9, 9, 8));
    }

}