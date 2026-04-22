package microarch.delivery.core.domain.model.order.events;

import libs.ddd.DomainEvent;
import microarch.delivery.core.domain.model.order.Order;

public class OrderCompletedDomainEvent extends DomainEvent<Order> {
    public OrderCompletedDomainEvent(Order source) {
        super(source);
    }
}
