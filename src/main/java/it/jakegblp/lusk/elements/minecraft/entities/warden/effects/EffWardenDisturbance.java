package it.jakegblp.lusk.elements.minecraft.entities.warden.effects;

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
import org.jetbrains.annotations.Nullable;

@Name("Warden - Disturb")
@Description("Makes a warden sense a disturbance at the provided location.")
@Examples({"""
        disturb {_warden} from {_location}
        """})
@Since("1.0.2")
@SuppressWarnings("unused")
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
        return "disturb " + entitiesExpression.toString(event, debug) + " from " + locationExpression.toString(event, debug);
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