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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Zombify Head Shake")
@Description("Make a villager shake his head.")
@Examples({"""
           make target shake head"""})
@Since("1.0.2+")
public class EffVillagerHeadShake extends Effect {
    static {
        Skript.registerEffect(EffVillagerHeadShake.class, "make %entities% shake head","shake %entities%'[s] head");
    }

    private Expression<Entity> entityExpression;
    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        entityExpression = (Expression<Entity>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + (event == null ? "" : entityExpression.getArray(event)) + " shake their head";
    }

    @Override
    protected void execute(@NotNull Event event) {
        Entity[] entities = entityExpression.getArray(event);
        for (Entity entity : entities) {
            if (entity instanceof Villager villager) {
                villager.shakeHead();
            }
        }
    }
}