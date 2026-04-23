package microarch.delivery;

import libs.ddd.Aggregate;
import libs.ddd.DomainEvent;
import libs.ddd.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
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