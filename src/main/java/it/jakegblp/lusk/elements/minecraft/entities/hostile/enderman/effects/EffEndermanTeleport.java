package it.jakegblp.lusk.elements.minecraft.entities.hostile.enderman.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Enderman - Randomly Teleport")
@Description("Attempts to teleport an enderman to a random nearby location.")
@Examples({"""
        randomly teleport (entities where [input is an enderman])"""})
@Since("1.0.2")
public class EffEndermanTeleport extends Effect {
    static {
        Skript.registerEffect(EffEndermanTeleport.class, "[attempt to] randomly teleport %livingentities%");
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
        return "attempt to randomly teleport " + (event == null ? "" : entityExpression.toString(event, debug));
    }

    @Override
    protected void execute(@NotNull Event event) {
        LivingEntity[] entities = entityExpression.getArray(event);
        for (LivingEntity entity : entities) {
            if (entity instanceof Enderman enderman) {
                enderman.teleportRandomly();
            }
        }
    }
}