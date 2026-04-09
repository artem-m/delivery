package microarch.delivery.core.application.queries;

import microarch.delivery.core.domain.model.Location;

import java.util.Collection;
import java.util.UUID;

public record GetAllCouriersResponse(Collection<Item> items) {

    public record Item(UUID id, String name, Location location) {

    }
}
