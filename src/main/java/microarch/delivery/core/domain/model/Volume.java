package microarch.delivery.core.domain.model;

import libs.ddd.ValueObject;
import libs.errs.Error;
import libs.errs.Guard;
import libs.errs.Result;

import java.util.List;

public class Volume extends ValueObject<Volume> {

    private final int vol;
    private final Iterable<Object> components;

    public static Volume create(int vol) {
        if (vol <= 0) {
            throw new IllegalArgumentException("Volume can not be negative");
        }
        return new Volume(vol);
    }

    private Volume(int vol) {
        this.vol = vol;
        this.components = List.of(vol);
    }

    public Result<Volume, Error> addTo(Volume other) {
        Error err = Guard.againstNull(other, "other");
        if (err != null) {
            return Result.failure(err);
        }
        return Result.success(new Volume(vol + other.vol));
    }

    @Override
    protected Iterable<Object> equalityComponents() {
        return components;
    }
}
