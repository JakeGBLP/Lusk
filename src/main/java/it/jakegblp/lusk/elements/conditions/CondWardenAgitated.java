package it.jakegblp.lusk.elements.conditions;

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
        return entity instanceof Warden warden && warden.getAngerLevel() == Warden.AngerLevel.AGITATED;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "agitated";
    }
}