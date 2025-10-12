package it.jakegblp.lusk.nms.core.world.entity.flags.entity;

import it.jakegblp.lusk.common.Validatable;
import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.util.NMSObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.function.Supplier;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@NoArgsConstructor
public enum RelativeFlag implements Validatable<IllegalArgumentException>, NMSObject<Enum<?>> {
    X,
    Y,
    Z,
    Y_ROT,
    X_ROT,
    @Availability(addedIn = "1.21.2")
    DELTA_X,
    @Availability(addedIn = "1.21.2")
    DELTA_Y,
    @Availability(addedIn = "1.21.2")
    DELTA_Z,
    @Availability(addedIn = "1.21.2")
    ROTATE_DELTA;

    @Getter
    private static final boolean revamped = NMS.getVersion().isGreaterOrEqual(Version.of(1, 21, 2));
    @Getter
    private static final @Unmodifiable List<RelativeFlag> XYZ = List.of(X, Y, Z);
    private static final @Unmodifiable List<RelativeFlag> DELTA = List.of(DELTA_X, DELTA_Y, DELTA_Z);

    public static @Unmodifiable List<RelativeFlag> getDelta() {
        return DELTA;
    }

    public boolean isAnyDelta() {
        return switch (this) {
            case DELTA_X, DELTA_Y, DELTA_Z, ROTATE_DELTA -> true;
            default -> false;
        };
    }

    @Override
    public boolean check() {
        return isAnyDelta() && isRevamped();
    }

    @Override
    public Supplier<IllegalArgumentException> getExceptionSupplier() {
        return () -> new IllegalArgumentException("This movement flag (" + this + ") cannot be used until 1.21.2");
    }

    @Override
    public Enum<?> asNMS() {
        return (Enum<?>) NMS.asNMSRelativeFlag(this);
    }
}
