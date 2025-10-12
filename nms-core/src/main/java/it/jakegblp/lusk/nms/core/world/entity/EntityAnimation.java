package it.jakegblp.lusk.nms.core.world.entity;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.annotations.Availability;
import it.jakegblp.lusk.common.CanonicallyOrdered;
import it.jakegblp.lusk.common.Validatable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@AllArgsConstructor
public enum EntityAnimation implements Validatable<IllegalArgumentException>, CanonicallyOrdered {
    SWING_MAIN_HAND(0),
    @Availability(removedIn = "1.19.4")
    HURT(1),
    WAKE_UP(2),
    SWING_OFF_HAND(3),
    CRITICAL_HIT(4),
    MAGICAL_CRITICAL_HIT(5);

    private final int actionId;

    public static EntityAnimation fromId(int id) {
        for (EntityAnimation animation : values())
            if (animation.actionId == id) {
                animation.validate();
                return animation;
            }
        return null;
    }

    @Override
    public boolean check() {
        return this != HURT || NMS.getVersion().isLowerThan(Version.of(1, 19, 4));
    }

    @Override
    public Supplier<IllegalArgumentException> getExceptionSupplier() {
        return () -> new IllegalArgumentException("'HURT' entity animation cannot be used past 1.19.3");
    }

    @Override
    public int getCanonicalOrder() {
        int ordinal = this.ordinal();
        return ordinal == 0 ? ordinal : check() ? ordinal - 1 : ordinal;
    }
}
