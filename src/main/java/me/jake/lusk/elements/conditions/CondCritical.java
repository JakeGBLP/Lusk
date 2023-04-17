package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.jake.lusk.utils.Utils;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("is Critical")
@Description("Checks if the damage is critical.\nCan be used in damage/death/destroy events.\n\nRead https://minecraft.fandom.com/wiki/Damage#Critical_hit for more info.")
@Examples({"on damage:\n\tif critical:\n\t\tbroadcast \"ouch!\""})
@Since("1.0.0, 1.0.2+ (Fixed and Expanded)")
public class CondCritical extends Condition {
    static {
        Skript.registerCondition(CondCritical.class, "[the] damage is critical",
                                                             "[the] damage is(n't| not) critical");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!getParser().isCurrentEvent(EntityDamageEvent.class, EntityDeathEvent.class, VehicleDamageEvent.class, VehicleDestroyEvent.class)) {
            Skript.error("This condition can only be used in damage/death/destroy events.");
            return false;
        }
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the damage is" + (isNegated() ? " not" : "") + " critical";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return isNegated() ^ Utils.isCritical(event);
    }
}