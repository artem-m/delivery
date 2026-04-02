package microarch.delivery.adapters.out;

import lombok.RequiredArgsConstructor;
import microarch.delivery.adapters.out.postgres.CourierJpaRepository;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.ports.CourierRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourierRepositoryImpl implements CourierRepository {
    private final CourierJpaRepository courierJpaRepository;

    @Override
    public Courier create(Courier entity) {
        return courierJpaRepository.save(entity);
    }

    @Override
    public Courier update(Courier entity) {
        return courierJpaRepository.save(entity);
    }

    @Override
    public Optional<Courier> get(UUID id) {
        return courierJpaRepository.findById(id);
    }

    @Override
    public Collection<Courier> findAll() {
        return courierJpaRepository.findAll();
    }
}
