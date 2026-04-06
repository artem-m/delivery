package microarch.delivery.core.application.commands;

import libs.errs.Guard;

import java.util.UUID;

public record CompleteOrderCommand(UUID courierId, UUID orderId) {
    public CompleteOrderCommand {
        var error = Guard.combine(Guard.againstNull(courierId, "courierId"), Guard.againstNull(orderId, "orderId"));
        if (error != null) {
            throw new IllegalArgumentException(error.getMessage());
        }
    }
}
