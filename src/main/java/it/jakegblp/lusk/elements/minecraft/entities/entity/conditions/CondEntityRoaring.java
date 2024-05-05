package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pose;
import org.bukkit.entity.Ravager;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Roaring")
@Description("Checks if an entity is roaring.)")
@Examples("on damage of ravager:\n\tvictim is roaring\n\tkill victim")
@Since("1.1.1")
public class CondEntityRoaring extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityRoaring.class, "roaring", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity instanceof Ravager ravager) {
            return ravager.getRoarTicks() > 0;
        } else if (entity instanceof EnderDragon enderDragon) {
            return enderDragon.getPhase() == EnderDragon.Phase.ROAR_BEFORE_ATTACK;
        } else {
            return entity.getPose() == Pose.ROARING;
        }
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "roaring";
    }
}