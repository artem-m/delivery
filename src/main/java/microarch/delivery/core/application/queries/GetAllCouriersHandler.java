package microarch.delivery.core.application.queries;

import libs.errs.Error;
import libs.errs.Result;
import lombok.RequiredArgsConstructor;
import microarch.delivery.core.ports.CourierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class GetAllCouriersHandler implements Function<GetAllCouriersQuery, Result<GetAllCouriersResponse, Error>> {
    private final CourierRepository courierRepository;

    @Override
    @Transactional(readOnly = true)
    public Result<GetAllCouriersResponse, Error> apply(GetAllCouriersQuery query) {
        var items = courierRepository.findAll().stream()
                .map(it -> new GetAllCouriersResponse.Item(it.getId(), it.getName(), it.getLocation())).toList();
        // empty list is ok response
        return Result.success(new GetAllCouriersResponse(items));
    }
}
