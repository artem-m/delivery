package microarch.delivery.core.application.commands;

import microarch.delivery.core.domain.model.courier.Courier;
import microarch.delivery.core.ports.CourierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCourierHandlerTest {
    @InjectMocks
    CreateCourierHandler handler;
    @Mock
    CourierRepository courierRepository;

    @Test
    public void shouldHandleOk() {
        String someName = "some name";
        when(courierRepository.create(any(Courier.class))).thenReturn(null);
        var result = handler.apply(new CreateCourierCommand(someName));
        assertThat(result.isFailure()).isFalse();
    }
}