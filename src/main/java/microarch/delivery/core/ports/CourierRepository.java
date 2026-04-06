package microarch.delivery.core.ports;

import microarch.delivery.core.domain.model.courier.Courier;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface CourierRepository {
    Courier create(Courier entity);

    Courier update(Courier entity);

    Optional<Courier> get(UUID id);

    Collection<Courier> findAll(); // offset, limit
}
