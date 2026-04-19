package microarch.delivery.core.application.commands;

import libs.ddd.BaseEntity;
import lombok.RequiredArgsConstructor;
import microarch.delivery.core.domain.model.Location;
import microarch.delivery.core.domain.model.courier.Assignment;
import microarch.delivery.core.ports.CourierRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class MoveCouriersUseCase {
    private final MoveCourierHandler handler;
    private final CourierRepository courierRepository;

    public void handle() {
        var moves = courierRepository.findAll().stream().collect(Collectors.toMap(BaseEntity::getId,
                it -> stepToFirstAssignment(it.getLocation(), it.getAssignments())));
        for (var entry : moves.entrySet()) {
            handler.apply(new MoveCourierCommand(entry.getKey(), entry.getValue()));
        }
    }

    private Location stepToFirstAssignment(Location src, List<Assignment> assignments) {
        if (assignments == null || assignments.isEmpty()) {
            return src;
        }
        for (Assignment assignment : assignments) {
            var dst = src.stepTo(assignment.getLocation());
            if (!Objects.equals(src, dst)) {
                return dst;
            }
        }
        return src;

    }
}
