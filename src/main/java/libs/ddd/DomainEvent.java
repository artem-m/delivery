package libs.ddd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class DomainEvent<T> extends ApplicationEvent {
    private final UUID eventId = UUID.randomUUID();
    private final Instant occurredOnUtc = Instant.now();

    public DomainEvent(T source) {
        super(source);
    }

    // Fake Ctr for Jackson / JPA
    protected DomainEvent() {
        super("default");
    }

    @JsonIgnore
    @Override
    public T getSource() {
        return (T) super.getSource();
    }
}