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
import org.bukkit.entity.Allay;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;

@Name("Allay Dance")
@Description("Forces an Allay to start/stop dancing.\nIf the jukebox is specified but the provided block is not a jukebox, the Allay will start dancing without one.")
@Examples({"""
           make target start dancing"""})
@Since("1.0.2+")
public class EffAllayDance extends Effect {
    static {
        Skript.registerEffect(EffAllayDance.class,
                "make %livingentities% start dancing",
                "make %livingentities% stop dancing");
    }
    private Expression<LivingEntity> entityExpression;
    private int pattern;
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        pattern = matchedPattern;
        entityExpression = (Expression<LivingEntity>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + (event == null ? "" : Arrays.toString(entityExpression.getArray(event))) + (pattern == 0 || pattern == 1 ? " start" : " stop") + " dancing";
    }

    @Override
    protected void execute(@NotNull Event event) {
        LivingEntity[] entities = entityExpression.getArray(event);
        for (LivingEntity entity : entities) {
            if (entity instanceof Allay allay) {
                if (pattern == 0) {
                    allay.startDancing();
                } else {
                    allay.stopDancing();
                }
            }
        }
    }
}