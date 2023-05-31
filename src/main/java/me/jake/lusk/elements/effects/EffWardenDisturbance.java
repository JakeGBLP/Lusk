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
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Warden;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;

@Name("Warden Disturbance")
@Description("Makes the warden sense a disturbance at the provided location.")
@Examples({"""
        """})
@Since("1.0.2")
public class EffWardenDisturbance extends Effect {
    static {
        Skript.registerEffect(EffWardenDisturbance.class, "disturb %livingentities% from %location%");
    }

    private Expression<LivingEntity> entitiesExpression;
    private Expression<Location> locationExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        entitiesExpression = (Expression<LivingEntity>) expressions[0];
        locationExpression = (Expression<Location>) expressions[1];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "disturb " + (event == null ? "" : Arrays.toString(entitiesExpression.getArray(event))) + " from " + (event == null ? "" : locationExpression.getSingle(event));
    }

    @Override
    protected void execute(@NotNull Event event) {
        Location location = locationExpression.getSingle(event);
        if (location == null) return;
        for (LivingEntity entity : entitiesExpression.getArray(event)) {
            if (entity instanceof Warden warden) {
                warden.setDisturbanceLocation(location);
            }
        }
    }
}