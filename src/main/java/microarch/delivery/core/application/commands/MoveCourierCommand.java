package microarch.delivery.core.application.commands;

import libs.errs.Guard;
import microarch.delivery.core.domain.model.Location;

import java.util.UUID;

public record MoveCourierCommand(UUID courierId, Location location) {
    public MoveCourierCommand {
        var error = Guard.combine(Guard.againstNull(courierId, "courierId"), Guard.againstNull(location, "location"));
        if (error != null) {
            throw new IllegalArgumentException(error.getMessage());
        }
    }
}
