package me.jake.lusk.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Zombify Villager")
@Description("Converts Villagers into Zombie Villagers as if they were killed by a Zombie.")
@Examples({"""
           zombify target"""})
@Since("1.0.2+")
public class EffVillagerZombify extends Effect {
    static {
        Skript.registerEffect(EffVillagerZombify.class, "zombify %livingentities%");
    }

    private Expression<LivingEntity> entityExpression;
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        entityExpression = (Expression<LivingEntity>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "zombify " + (event == null ? "" : entityExpression.getArray(event));
    }

    @Override
    protected void execute(@NotNull Event event) {
        LivingEntity[] entities = entityExpression.getArray(event);
        for (LivingEntity entity : entities) {
            if (entity instanceof Villager villager) {
                villager.zombify();
            }
        }
    }
}