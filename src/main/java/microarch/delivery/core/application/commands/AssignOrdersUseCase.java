package microarch.delivery.core.application.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignOrdersUseCase {
    private final AssignOrderHandler handler;

    public void handle() {
        handler.apply(new AssignOrderCommand());
    }
}
