package microarch.delivery.core.domain.model.order2;

import libs.ddd.BaseEntity;
import libs.errs.Error;
import libs.errs.Guard;
import libs.errs.Result;
import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.Volume;

import java.util.UUID;

public class Order extends BaseEntity<UUID> {

    private Location location;
    private Volume capacity;
    // product(s)?

    private Order(Location location, Volume capacity) {
        super(UUID.randomUUID());
        this.location = location;
        this.capacity = capacity;
    }

    public static Result<Order, Error> create(Location location, Volume maxCapacity) {
        Error err = Guard.combine(Guard.againstNull(location, "location"),
                Guard.againstNull(maxCapacity, "maxCapacity"));
        if (err != null) {
            return Result.failure(err);
        }
        return Result.success(new Order(location, maxCapacity));
    }

    public Volume getCapacity() {
        return capacity;
    }

    public Location getLocation() {
        return location;
    }
}
