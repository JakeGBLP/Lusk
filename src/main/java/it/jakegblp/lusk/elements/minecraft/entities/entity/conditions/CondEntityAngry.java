package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.EntityUtils.isAngry;

@Name("Entity - is Angry")
@Description("Checks if an entity is angry.\n(Warden, PigZombie, Wolf, Enderman)")
@Examples({"on damage of wolf:\n\tif victim is angry:\n\t\tcancel event"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class CondEntityAngry extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityAngry.class, "angry", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return isAngry(entity);
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "angry";
    }
}