package microarch.delivery.adapters.in.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microarch.delivery.core.application.commands.CreateOrderCommand;
import microarch.delivery.core.application.commands.CreateOrderHandler;
import microarch.delivery.core.domain.model.Address;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import queues.basket.BasketEventsProto;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketEventsConsumer {

    private final ObjectMapper mapper;
    private final CreateOrderHandler handler;

    @KafkaListener(topics = "${app.kafka.baskets-events-topic}", concurrency = "1", idIsGroup = false, groupId = "${spring.application.name}")
    public void receiveBasketEvent(ConsumerRecord<byte[], String> record) {
        BasketEventsProto.BasketConfirmedIntegrationEvent payload = null;
        try {
            payload = mapper.readValue(record.value(), BasketEventsProto.BasketConfirmedIntegrationEvent.class);
        } catch (JsonProcessingException e) {
            log.error("Cant parse payload for record key={}", record.key());
            // let commit offset
            return;
        }

        var result = handler.apply(new CreateOrderCommand(UUID.randomUUID(),
                Address.create(payload.getAddress().getCountry(), payload.getAddress().getCity(),
                        payload.getAddress().getStreet(), payload.getAddress().getHouse(),
                        payload.getAddress().getApartment()),
                payload.getVolume()));
        if (result.isFailure()) {
            log.error("Failed to process payload={}", payload);
        }
    }
}
