package it.jakegblp.lusk.elements.minecraft.entities.ocelot.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.jetbrains.annotations.NotNull;

@Name("Ocelot - is Trusting")
@Description("Checks if an ocelot trusts players.")
@Examples({"on damage of ocelot:\n\tif victim is trusting:\n\t\tcancel event\n\t\tbroadcast \"Don't betray %victim%!\""})
@Since("1.0.2")
@SuppressWarnings("unused")
public class CondOcelotTrusting extends PropertyCondition<LivingEntity> {
    static {
        register(CondOcelotTrusting.class, "trusting", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Ocelot ocelot && ocelot.isTrusting();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "trusting";
    }
}