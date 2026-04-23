package microarch.delivery.adapters.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microarch.delivery.core.domain.model.order.events.OrderCompletedDomainEvent;
import microarch.delivery.core.domain.model.order.events.OrderCreatedDomainEvent;
import microarch.delivery.core.ports.OrderEventsPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import queues.order.OrderEventsProto;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderEventsKafkaProducer implements OrderEventsPublisher {

    private final ObjectMapper mapper;
    private final KafkaTemplate<String, byte[]> template;
    @Value("${app.kafka.orders-events-topic}")
    private final String topic;

    @EventListener
    public void onOrderCompleted(OrderCompletedDomainEvent event) throws JsonProcessingException {
        log.info("Received event={}", event);
        var payload = OrderEventsProto.OrderCompletedIntegrationEvent.newBuilder()
                .setOrderId(event.getSource().getId().toString()).build();
        sendToTopic( event.getSource().getId().toString(),payload);
    }

    @EventListener
    public void onOrderCreated(OrderCreatedDomainEvent event) throws JsonProcessingException {
        log.info("Received event={}", event);
        var payload = OrderEventsProto.OrderCreatedIntegrationEvent.newBuilder()
                .setOrderId(event.getSource().getId().toString()).build();
        sendToTopic( event.getSource().getId().toString(),payload);
    }

    private void sendToTopic(String key, Object payload) throws JsonProcessingException {
        template.send(topic, key, mapper.writeValueAsBytes(payload))
                .whenComplete((it, throwable) -> {
                    if (throwable != null) {
                        log.error("Failed to send={} event", payload, throwable);
                    } else {
                        log.info("Event={} sent", payload);
                    }
                });
    }
}
