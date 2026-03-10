package microarch.delivery.core.domain.model;

import libs.ddd.BaseEntity;
import libs.errs.Error;
import libs.errs.Guard;
import libs.errs.Result;

import java.util.Objects;
import java.util.UUID;

public class Assignment extends BaseEntity<UUID> {

    private UUID orderId;
    private Volume volume;
    private Location location;
    private Status status;

    public static Result<Assignment, Error> createFor(Order order) {
        return create(order.getId(), order.getCapacity(), order.getLocation());
    }

    public static Result<Assignment, Error> create(UUID orderId, Volume volume, Location location) {
        Error err = Guard.combine(Guard.againstNullOrEmpty(orderId, "orderId"), Guard.againstNull(volume, "volume"),
                Guard.againstNull(location, "location"));
        if (err != null) {
            return Result.failure(err);
        }
        return Result.success(new Assignment(orderId, volume, location));
    }

    private Assignment(UUID orderId, Volume volume, Location location) {
        super(UUID.randomUUID());
        this.orderId = orderId;
        this.volume = volume;
        this.location = location;
        this.status = Status.Assigned;
    }

    public boolean isBelongsTo(Order order) {
        return order != null && Objects.equals(orderId, order.getId());
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Volume getVolume() {
        return volume;
    }

    public Location getLocation() {
        return location;
    }

    public Status getStatus() {
        return status;
    }

    public String toString() {
        return "Assignment(id=" + this.getId() + ", orderId=" + this.getOrderId() + ", volume=" + this.getVolume()
                + ", location=" + this.getLocation() + ", status=" + this.getStatus() + ")";
    }

    public enum Status {
        Assigned, Completed
    }
}
