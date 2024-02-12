package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Will Be Damaged")
@Description("This Condition requires Paper.\n\nChecks whether or not the entity in the Pre Damage Event will be damaged.")
@Examples({"""
        """})
@Since("1.0.0")
public class CondWillBeDamaged extends Condition {

    static {
        if (Skript.classExists("io.papermc.paper.event.player.PrePlayerAttackEntityEvent")) {
            Skript.registerCondition(CondWillBeDamaged.class, "[the] entity will be (damaged|attacked)",
                    "[the] entity w(ill not|on't) be (damaged|attacked)");
        }
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!(getParser().isCurrentEvent(PrePlayerAttackEntityEvent.class))) {
            Skript.error("This condition can only be used in the Pre Damage event!");
            return false;
        }
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the entity " + (isNegated() ? "will not" : "will") + " be damaged";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return (isNegated()) ^ ((PrePlayerAttackEntityEvent) event).willAttack();
    }
}