package microarch.delivery.adapters.out.postgres.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import libs.ddd.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class Job {

    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;
    private final OutboxMessageRepository repository;

    // add shedlock
    @Scheduled(fixedDelay = 5_000)
    @Transactional
    public void publishSavedMessages() {
        log.info("Started publishSavedMessages");
        var messages = repository.findByProcessedOnUtcIsNull();
        for (var outboxMessage : messages) {
            log.info("Processing outboxMessage={}", outboxMessage);

            try {
                var eventClassName = outboxMessage.getEventType();
                var eventClass = Class.forName(eventClassName);
                var eventObject = objectMapper.readValue(outboxMessage.getPayload(), eventClass);
                if (!(eventObject instanceof DomainEvent domainEvent)) {
                    log.error("Invalid outbox message type: {}",  eventClass);
                    continue;
                }
                publisher.publishEvent(domainEvent);
                outboxMessage.markAsProcessed();
                repository.save(outboxMessage);
            } catch (Exception e) {
                log.error("Failed to publish outbox message: {}", outboxMessage, e);
            }
        }
    }
}
