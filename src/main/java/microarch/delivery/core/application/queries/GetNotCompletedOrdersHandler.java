package microarch.delivery.core.application.queries;

import libs.errs.Error;
import libs.errs.Result;
import lombok.RequiredArgsConstructor;
import microarch.delivery.core.ports.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GetNotCompletedOrdersHandler
        implements Function<GetNotCompletedOrdersCommand, Result<GetNotCompletedOrdersResponse, Error>> {
    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public Result<GetNotCompletedOrdersResponse, Error> apply(GetNotCompletedOrdersCommand command) {
        var items = orderRepository.findAllNotCompleted().stream()
                .map(it -> new GetNotCompletedOrdersResponse.Item(it.getId(), it.getLocation())).toList();
        return Result.success(new GetNotCompletedOrdersResponse(items));
    }
}
