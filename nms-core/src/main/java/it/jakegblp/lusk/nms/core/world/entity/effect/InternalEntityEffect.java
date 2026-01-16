package it.jakegblp.lusk.nms.core.world.entity.effect;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@Getter
public enum InternalEntityEffect {
    PLAYER_DEBUG_REDUCED_ENABLE(22),
    PLAYER_DEBUG_REDUCED_DISABLE(23),
    PLAYER_OP_LEVEL_0(24),
    PLAYER_OP_LEVEL_1(25),
    PLAYER_OP_LEVEL_2(26),
    PLAYER_OP_LEVEL_3(27),
    PLAYER_OP_LEVEL_4(28);

    private final byte data;
    private final Set<Class<? extends Entity>> applicableClasses;

    @SafeVarargs
    InternalEntityEffect(int data, Class<? extends Entity>... applicableClasses) {
        Preconditions.checkState(applicableClasses.length > 0, "Unknown applicable classes");
        this.data = (byte) data;
        this.applicableClasses = Set.of(applicableClasses);
    }

    InternalEntityEffect(int data) {
        this(data, Player.class);
    }

    public boolean isApplicableTo(@NotNull Entity entity) {
        return isApplicableTo(entity.getClass());
    }

    public boolean isApplicableTo(@NotNull Class<? extends Entity> clazz) {
        for (Class<? extends Entity> applicableClass : this.applicableClasses)
            if (applicableClass.isAssignableFrom(clazz))
                return true;
        return false;
    }

}
