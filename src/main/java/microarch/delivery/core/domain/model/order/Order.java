package microarch.delivery.core.domain.model.order;

import libs.ddd.BaseEntity;
import libs.errs.Error;
import libs.errs.Guard;
import libs.errs.Result;
import libs.errs.UnitResult;
import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.Volume;

import java.util.UUID;

public class Order extends BaseEntity<UUID> {

    private Location location;
    private Volume volume;
    private OrderStatus status;
    // product(s)?

    private Order(UUID id, Location location, Volume capacity) {
        super(id);
        this.location = location;
        this.volume = capacity;
        this.status = OrderStatus.Created;
    }

    public static Result<Order, Error> create(UUID id, Location location, Volume volume) {
        Error err = Guard.combine(Guard.againstNull(location, "location"), Guard.againstNull(id, "id"),
                Guard.againstNull(volume, "volume"));
        if (err != null) {
            return Result.failure(err);
        }
        return Result.success(new Order(id, location, volume));
    }

    public boolean isCreated() {
        return status == OrderStatus.Created;
    }

    public UnitResult<Error> assign() {
        if (status != OrderStatus.Created) {
            return UnitResult.failure(Errors.invalidStatus(status, OrderStatus.Assigned));
        }
        status = OrderStatus.Assigned;
        return UnitResult.success();
    }

    public UnitResult<Error> complete() {
        if (status != OrderStatus.Assigned) {
            return UnitResult.failure(Errors.invalidStatus(status, OrderStatus.Completed));
        }
        status = OrderStatus.Completed;
        return UnitResult.success();
    }

    public Volume getVolume() {
        return volume;
    }

    public Location getLocation() {
        return location;
    }

    public enum OrderStatus {
        Created, Assigned, Completed
    }

    static class Errors {
        static Error invalidStatus(OrderStatus src, OrderStatus dst) {
            return Error.of("order.can.not.change.status", "Can not change status from %s to %s".formatted(src, dst));
        }

    }
}
