package microarch.delivery.core.ports;

import libs.errs.Error;
import libs.errs.Result;
import microarch.delivery.core.domain.model.Location;

public interface GeoServiceClient {

    Result<Location, Error> getGeoLocation(String street);

}
