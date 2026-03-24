package microarch.delivery.core.domain.model;

public class Location {

    public static final int MIN_COORD_VALUE = 1;
    public static final int MAX_COORD_VALUE = 10;
    private final int x;
    private final int y;

    private Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Location create(int x, int y) {
        if (x < MIN_COORD_VALUE || x > MAX_COORD_VALUE) {
            throw new IllegalArgumentException("Invalid location, x should be between 1 and 10, but got = " + x);
        }
        if (y < MIN_COORD_VALUE || y > MAX_COORD_VALUE) {
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

    public boolean canReach(Location other, int steps) {
        int diffX = Math.abs(this.x - other.x);
        int diffY = Math.abs(this.y - other.y);
        return Math.max(diffX, diffY) <= steps;
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

    @Override
    public String toString() {
        return "Location(x=" + this.x + ", y=" + this.y + ")";
    }
}
