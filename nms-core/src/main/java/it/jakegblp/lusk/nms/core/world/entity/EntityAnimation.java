package it.jakegblp.lusk.nms.core.world.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityAnimation {
    SWING_MAIN_HAND(0),
    WAKE_UP(2),
    SWING_OFF_HAND(3),
    CRITICAL_HIT(4),
    MAGICAL_CRITICAL_HIT(5);

    private final int actionId;

    public static EntityAnimation fromId(int id) {
        for (EntityAnimation animation : values())
            if (animation.actionId == id) {
                return animation;
            }
        return null;
    }
}
