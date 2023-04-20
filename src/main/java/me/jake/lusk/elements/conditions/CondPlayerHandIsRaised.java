package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Hand Raised")
@Description("Checks if an entity is raising its hand.")
@Examples({"if target is raising its hand:\n\tbroadcast \"THANK YOU FOR LENDING ME YOUR ENERGY!\""})
@Since("1.0.3")
public class CondPlayerHandIsRaised extends PropertyCondition<Entity> {
    static {
        register(CondPlayerHandIsRaised.class, "raising [their|its] hand", "entity");
    }

    @Override
    public boolean check(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity.isHandRaised();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "raising its hand";
    }
}