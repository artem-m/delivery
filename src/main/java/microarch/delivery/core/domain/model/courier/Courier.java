package microarch.delivery.core.domain.model.courier;

import libs.ddd.BaseEntity;
import libs.errs.Error;
import libs.errs.Guard;
import libs.errs.Result;
import libs.errs.UnitResult;
import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.Volume;
import microarch.delivery.core.domain.model.order.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Courier extends BaseEntity<UUID> {
    public static final int MAX_STEPS_IN_MOVE = 1;
    public static final Volume MAX_VOLUME = Volume.create(20);
    private String name;
    private List<Assignment> assignments = new ArrayList<>();
    private Location location;
    private Volume maxVolume;

    private Courier(String name, Location location, Volume maxVolume) {
        super(UUID.randomUUID());
        this.name = name;
        this.location = location;
        this.maxVolume = maxVolume;
    }

    public static Result<Courier, Error> create(String name, Location location) {
        Error err = Guard.combine(Guard.againstNull(location, "location"), Guard.againstNullOrEmpty(name, "name"));
        if (err != null) {
            return Result.failure(err);
        }
        return Result.success(new Courier(name, location, MAX_VOLUME));
    }

    public UnitResult<Error> assign(Order order) {
        for (Assignment assignment : assignments) {
            if (assignment.isBelongsTo(order)) {
                return UnitResult.failure(Errors.alreadyAssigned(order));
            }
        }
        var volumeResult = maxVolume.minus(order.getVolume());
        if (volumeResult.isFailure()) {
            return UnitResult.failure(volumeResult.getError());
        }
        maxVolume = volumeResult.getValue();
        var assignResult = Assignment.createFor(order);
        if (assignResult.isFailure()) {
            return UnitResult.failure(assignResult.getError());
        }
        assignments.add(assignResult.getValue());
        return order.assign();
    }

    public UnitResult<Error> complete(Order order) {
        var assignment = assignments.stream().filter(it -> it.isBelongsTo(order)).findFirst().orElse(null);
        if (assignment == null) {
            return UnitResult.failure(Errors.canNotCompleteWrongOrder());
        }
        maxVolume = maxVolume.addTo(order.getVolume()).getValue();
        return assignment.complete(location);
    }

    public UnitResult<Error> moveTo(Location dst) {
        if (!canMoveTo(dst)) {
            return UnitResult.failure(Errors.canNotMoveTo(dst));
        }
        this.location = dst;
        // actualize finished assignments ?
        return UnitResult.success();
    }

    private boolean canMoveTo(Location dst) {
        return location.canReach(dst, MAX_STEPS_IN_MOVE);
    }

    public List<Assignment> getAssignments() {
        return Collections.unmodifiableList(this.assignments);
    }

    public Location getLocation() {
        return this.location;
    }

    public Volume getMaxVolume() {
        return this.maxVolume;
    }

    static class Errors {
        static Error canNotMoveTo(Location dst) {
            return Error.of("couriers.invalid.destination", "Courier can not move to destination location=" + dst);
        }

        static Error capacityExceeded(Order order) {
            return Error.of("couriers.capacity.exceeded", "Couriers capacity exceeded, can not accept order=" + order);
        }

        static Error alreadyAssigned(Order order) {
            return Error.of("couriers.order.already.assigned", "Couriers already has assignment to order=" + order);
        }

        static Error canNotCompleteWrongOrder() {
            return Error.of("courier.complete.wrong.order", "Can not complete order with no assignment");
        }
    }
}
