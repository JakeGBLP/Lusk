package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Warden;
import org.jetbrains.annotations.NotNull;

@Name("Warden - is Agitated")
@Description("Checks if a Warden is agitated.")
@Examples({"on damage of warden:\n\tif victim is agitated:\n\t\tbroadcast \"Uh oh\""})
@Since("1.0.2")
public class CondWardenAgitated extends PropertyCondition<LivingEntity> {
    static {
        register(CondWardenAgitated.class, "agitated", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity instanceof Warden warden) {
            return warden.getAngerLevel() == Warden.AngerLevel.AGITATED;
        } else {
            Skript.error("You can only use this condition with Wardens");
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "agitated";
    }
}