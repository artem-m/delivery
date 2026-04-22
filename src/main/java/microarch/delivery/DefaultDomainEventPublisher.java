package microarch.delivery;

import libs.ddd.Aggregate;
import libs.ddd.DomainEvent;
import libs.ddd.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
// @Component <- exclude from context
public class DefaultDomainEventPublisher implements DomainEventPublisher {
    private final ApplicationEventPublisher publisher;

    public DefaultDomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(Iterable<Aggregate<?>> aggregates) {
        for (Aggregate<?> aggregate : aggregates) {
            for (DomainEvent event : aggregate.getDomainEvents()) {
                log.info("Publishing event={} from aggregate={}", event, aggregate);
                publisher.publishEvent(event);
            }
        }
    }
}