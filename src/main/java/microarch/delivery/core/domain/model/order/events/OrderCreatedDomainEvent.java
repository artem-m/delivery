package microarch.delivery.core.domain.model.order.events;

import libs.ddd.DomainEvent;
import microarch.delivery.core.domain.model.order.Order;

public class OrderCreatedDomainEvent extends DomainEvent<Order> {
    public OrderCreatedDomainEvent(Order source) {
        super(source);
    }
}
