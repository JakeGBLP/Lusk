package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Goat;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Screaming")
@Description("Checks if an entity is screaming.\n(Enderman, Goat)")
@Examples({"on damage of enderman:\n\tif victim is screaming:\n\t\tcancel event"})
@Since("1.0.2")
public class CondEntityScreaming extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityScreaming.class, "screaming", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity instanceof Goat goat) {
            return goat.isScreaming();
        } else if (entity instanceof Enderman enderman) {
            return enderman.isScreaming();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "screaming";
    }
}