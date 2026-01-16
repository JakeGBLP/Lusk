package it.jakegblp.lusk.nms.core.world.entity.metadata.flags.entity;

import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.common.Validatable;
import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.common.annotations.Availability;
import it.jakegblp.lusk.nms.core.util.PureNMSObject;
import lombok.Getter;
import org.jetbrains.annotations.Unmodifiable;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
public enum RelativeFlag implements Validatable<IllegalArgumentException>, PureNMSObject<Enum<?>> {
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
    @Availability(addedIn = "1.21.2")
    private static final @Unmodifiable List<RelativeFlag> DELTA = List.of(DELTA_X, DELTA_Y, DELTA_Z);
    private static final RelativeFlag[] AVAILABLE_FLAGS = CommonUtils.filter(values(), RelativeFlag::check);

    public static @Unmodifiable List<RelativeFlag> getDelta() {
        return DELTA;
    }

    public static RelativeFlag[] getAvailableFlags() {
        return AVAILABLE_FLAGS;
    }

    public boolean isAnyDelta() {
        return switch (this) {
            case DELTA_X, DELTA_Y, DELTA_Z, ROTATE_DELTA -> true;
            default -> false;
        };
    }

    public int getMask() {
        return 1 << ordinal();
    }

    private boolean isSet(int mask) {
        return (mask & this.getMask()) == this.getMask();
    }

    public static Set<RelativeFlag> unpack(int mask) {
        Set<RelativeFlag> set = EnumSet.noneOf(RelativeFlag.class);
        for(RelativeFlag relative : values())
            if (relative.isSet(mask))
                set.add(relative);
        return set;
    }

    public static int pack(Set<RelativeFlag> flags) {
        int i = 0;
        for(RelativeFlag relative : flags)
            i |= relative.getMask();
        return i;
    }

    @Override
    public boolean check() {
        return !isAnyDelta() || isRevamped();
    }

    @Override
    public Supplier<IllegalArgumentException> getExceptionSupplier() {
        return () -> new IllegalArgumentException("This movement flag (" + this + ") cannot be used prior to 1.21.2");
    }
}
