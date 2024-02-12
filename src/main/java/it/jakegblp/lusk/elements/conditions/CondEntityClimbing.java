package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Climbing")
@Description("Checks if a living entity is climbing (Ladders, vines, spiders etc).")
@Examples({"if player is climbing:"})
@Since("1.0.2")
public class CondEntityClimbing extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityClimbing.class, "climbing", "livingentity");
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