package microarch.delivery.core.application.commands;

import libs.errs.Guard;

public record CreateCourierCommand(String name) {
    public CreateCourierCommand {
        var error = Guard.combine(Guard.againstNull(name, "name"));
        if (error != null) {
            throw new IllegalArgumentException(error.getMessage());
        }
    }
}
