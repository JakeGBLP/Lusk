package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Bat;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Sleeping")
@Description("Checks if one or more living entities are awake or sleeping.")
@Examples({"on damage:\n\tcancel event if victim is sleeping"})
@Since("1.0.0, 1.2 (Sleeping, Asleep)")
public class CondEntitySleeping extends PropertyCondition<LivingEntity> {

    static {
        register(CondEntitySleeping.class, "(:awake|asleep|sleeping)", "livingentities");
    }

    boolean awake;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        awake = parseResult.hasTag("awake");
        return true;
    }

    @Override
    public boolean check(LivingEntity entity) {
        boolean sleeping;
        if (entity instanceof Bat bat) {
            sleeping = !bat.isAwake();
        } else {
            sleeping = entity.isSleeping();
        }
        return awake ^ sleeping;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return awake ? "awake" : "sleeping";
    }
}