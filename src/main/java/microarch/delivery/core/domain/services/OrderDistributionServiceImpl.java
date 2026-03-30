package microarch.delivery.core.domain.services;

import libs.errs.Error;
import libs.errs.Guard;
import libs.errs.Result;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.order.Order;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OrderDistributionServiceImpl implements OrderDistributionService {
    @Override
    public Result<Courier, Error> assign(Order order, Collection<Courier> couriers) {
        var presenceError = Guard.againstNull(order, "order");
        if (presenceError != null) {
            return Result.failure(presenceError);
        }
        if (!order.isCreated()) {
            return Result.failure(Errors.invalidStatus());
        }
        Candidate candidate = new Candidate(null, Integer.MAX_VALUE);
        for (var courier : couriers) {
            var distance = order.getLocation().distanceTo(courier.getLocation());
            if (distance > candidate.distance()) {
                // it's too far
                continue;
            }
            if (courier.getMaxVolume().compareTo(order.getVolume()) < 0) {
                // it has no capacity
                continue;
            }
            candidate = new Candidate(courier, distance);
        }
        if (candidate.isFound()) {
            var result = candidate.courier().assign(order);
            if (result.isFailure()) {
                return Result.failure(result.getError());
            }
            result = order.assign();
            if (result.isFailure()) {
                return Result.failure(result.getError());
            }
            return Result.success(candidate.courier());
        }
        return Result.failure(Errors.noValidCandidates(order));
    }

    record Candidate(Courier courier, int distance) {
        boolean isFound() {
            return courier != null;
        }
    }

    static class Errors {
        static Error invalidStatus() {
            return Error.of("order.should.be.created", "Can assign order on in status=CREATED");
        }

        static Error noValidCandidates(Order order) {
            return Error.of("order.distribution.no.candidates",
                    "No valid candidates found for order with id=" + order.getId());
        }
    }
}
