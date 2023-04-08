package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("is Climbing")
@Description("Checks if a living entity is climbing (Ladders, vines, spiders etc).")
@Examples({"if player is climbing:"})
@Since("1.0.2")
public class CondClimbing extends PropertyCondition<LivingEntity> {

    static {
        register(CondClimbing.class, "climbing", "livingentity");
    }

    @Override
    public boolean check(LivingEntity livingEntity) {
        if (livingEntity != null) {
            return livingEntity.isClimbing();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "climbing";
    }
}