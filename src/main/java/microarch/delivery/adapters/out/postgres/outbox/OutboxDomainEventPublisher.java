package microarch.delivery.adapters.out.postgres.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import libs.ddd.Aggregate;
import libs.ddd.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OutboxDomainEventPublisher implements DomainEventPublisher {
    private final OutboxMessageRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(Iterable<Aggregate<?>> aggregates) {
        try {
            for (var aggregate : aggregates) {
                aggregate.getDomainEvents().forEach(domainEvent -> {
                    log.info("Publishing event={} from aggregate={}", domainEvent, aggregate);
                    try {
                        var payload = objectMapper.writeValueAsString(domainEvent);

                        var outboxMessage = new OutboxMessage(domainEvent.getEventId(),
                                domainEvent.getClass().getName(), aggregate.getId().toString(),
                                aggregate.getClass().getSimpleName(), payload, domainEvent.getOccurredOnUtc());
                        repository.save(outboxMessage);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to serialize domainEvent for Outbox", e);
                    }
                });

                aggregate.clearDomainEvents();
            }
        } catch (Exception e) {
            throw new RuntimeException("Persist events is failed", e);
        }
    }
}
