package microarch.delivery.core.domain.services;

import libs.errs.Error;
import libs.errs.Result;
import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.domain.model.order.Order;

import java.util.Collection;

public interface OrderDistributionService {
    Result<Courier, Error> assign(Order order, Collection<Courier> couriers);
}
