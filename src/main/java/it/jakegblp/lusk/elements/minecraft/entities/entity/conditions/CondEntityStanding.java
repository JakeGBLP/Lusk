package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Pose;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Standing")
@Description("Checks if an entity is standing, mainly polar bears, but other entities can be standing.")
@Examples({"on damage of polar bear:\n\tif victim is standing:\n\t\tcancel event\n\t\tsend \"you can't, %victim% is standing on business\" to attacker"})
@Since("1.2")
public class CondEntityStanding extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityStanding.class, "standing", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity instanceof PolarBear polarBear) {
            return polarBear.isStanding();
        }
        return entity.getPose().equals(Pose.STANDING);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "standing";
    }
}