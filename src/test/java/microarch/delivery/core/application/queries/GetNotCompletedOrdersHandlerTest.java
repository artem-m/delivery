package microarch.delivery.core.application.queries;

import microarch.delivery.core.BasePgIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class GetNotCompletedOrdersHandlerTest extends BasePgIntegrationTest {
    @Autowired
    GetNotCompletedOrdersHandler handler;

    @Test
    public void shouldHandleOk() {
        var result = handler.apply(new GetNotCompletedOrdersCommand());
        assertThat(result.isFailure()).isFalse();
    }
}