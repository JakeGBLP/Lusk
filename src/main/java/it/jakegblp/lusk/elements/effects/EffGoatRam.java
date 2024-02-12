package it.jakegblp.lusk.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Goat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Goat - Ram")
@Description("Makes a goat ram an entity.")
@Examples({"""
        make target ram {llama}"""})
@Since("1.0.3")
public class EffGoatRam extends Effect {
    static {
        Skript.registerEffect(EffGoatRam.class,
                "make %livingentities% ram %livingentity%");
    }

    private Expression<LivingEntity> entityExpression;
    private Expression<LivingEntity> entityExpression1;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        entityExpression = (Expression<LivingEntity>) expressions[0];
        entityExpression1 = (Expression<LivingEntity>) expressions[1];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + (event == null ? "" : entityExpression.getArray(event)) + " join the caravan of " + (event == null ? "" : entityExpression1.getArray(event));
    }

    @Override
    protected void execute(@NotNull Event event) {
        LivingEntity entity1 = entityExpression1.getSingle(event);
        if (entity1 == null) return;
        LivingEntity[] entities = entityExpression.getArray(event);
        for (LivingEntity entity : entities) {
            if (entity instanceof Goat goat) {
                goat.ram(entity1);
            }
        }
    }
}