package microarch.delivery.core.domain.model;

import libs.ddd.BaseEntity;
import libs.errs.Error;
import libs.errs.Guard;
import libs.errs.Result;
import libs.errs.UnitResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Courier extends BaseEntity<UUID> {
    public static final int MAX_STEPS_IN_MOVE = 1;
    private List<Assignment> assignments = new ArrayList<>();
    private Location location;
    private Volume maxCapacity;

    private Courier(Location location, Volume maxCapacity) {
        super(UUID.randomUUID());
        this.location = location;
        this.maxCapacity = maxCapacity;
    }

    public static Result<Courier, Error> create(Location location, Volume maxCapacity) {
        Error err = Guard.combine(Guard.againstNull(location, "location"),
                Guard.againstNull(maxCapacity, "maxCapacity"));
        if (err != null) {
            return Result.failure(err);
        }
        return Result.success(new Courier(location, maxCapacity));
    }

    public UnitResult<Error> assign(Order order) {
        var capacity = order.getCapacity();
        for (Assignment assignment : assignments) {
            if (assignment.isBelongsTo(order)) {
                return UnitResult.failure(Error.of("couriers.order.already.assigned",
                        "Couriers already has assignment to order=" + order));
            }
            var addResult = capacity.addTo(assignment.getVolume());
            if (addResult.isFailure()) {
                return UnitResult.failure(addResult.getError());
            }
            capacity = addResult.getValue();
        }
        if (capacity.compareTo(maxCapacity) > 0) {
            return UnitResult.failure(Error.of("couriers.capacity.exceeded",
                    "Couriers capacity exceeded, can not accept order=" + order));
        }
        var assignResult = Assignment.createFor(order);
        if (assignResult.isFailure()) {
            return UnitResult.failure(assignResult.getError());
        }
        assignments.add(assignResult.getValue());
        return UnitResult.success();
    }

    public UnitResult<Error> moveTo(Location dst) {
        if (!canMoveTo(dst)) {
            return UnitResult.failure(Error.of("couriers.invalid.destination",
                    "Courier can not move to destination location=" + location));
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
}
