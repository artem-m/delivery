package microarch.delivery.core.application.commands;

import libs.errs.Guard;
import microarch.delivery.core.domain.model.Address;

import java.util.UUID;

public record CreateOrderCommand(UUID orderId, Address address, int volume) {
    public CreateOrderCommand {
        var error = Guard.combine(Guard.againstNullOrEmpty(orderId, "orderId"), Guard.againstNull(address, "address"),
                Guard.againstLessThan(volume, 1, "orderId"));
        if (error != null) {
            throw new IllegalArgumentException(error.getMessage());
        }
    }
}
