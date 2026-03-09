package microarch.delivery.core.domain.model;

public class Location {

    private final int x;
    private final int y;

    private Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Location create(int x, int y) {
        if (x < 1 || x > 10) {
            throw new IllegalArgumentException("Invalid location, x should be between 1 and 10, but got = " + x);
        }
        if (y < 1 || y > 10) {
            throw new IllegalArgumentException("Invalid location, y should be between 1 and 10, but got = " + y);
        }

        return new Location(x, y);
    }

    public int distanceTo(Location other) {
        if (other == null) {
            throw new IllegalArgumentException("Can not calculate distance to NULL location");
        }
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location location) {
            return x == location.x && y == location.y;
        }
        return false;
    }
}
