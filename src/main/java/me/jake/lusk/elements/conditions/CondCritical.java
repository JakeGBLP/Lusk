package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;import ch.njol.skript.lang.SkriptParser.ParseResult;

import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("is Awake")
@Description("Checks if the damage is critical.")
@Examples({"on damage:\n\tif critical:\n\t\tbroadcast \"ouch!\""})
@Since("1.0.0")
public class CondCritical extends Condition {

    static {
        Skript.registerCondition(CondCritical.class, "[it(( i|')s [negated:not]| is[negated:(n'| no)t])|the [inflicted|caused] damage is[negated:(n'| no)t]] critical");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!(getParser().isCurrentEvent(EntityDamageByEntityEvent.class))) {
            Skript.error("This condition can only be used in the Damage event!");
            return false;
        }
        setNegated(parser.hasTag("negated"));
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "Damage Event is Critical";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return (isNegated()) ^ ((EntityDamageByEntityEvent) event).isCritical();
    }
}