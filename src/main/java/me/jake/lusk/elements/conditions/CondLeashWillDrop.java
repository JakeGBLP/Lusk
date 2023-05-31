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
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Leash - Will Be Dropped")
@Description("Checks whether or not the the leash will be dropped in the Unleash Event.")
@Examples({"""
        on unleash:
          if the leash will be dropped:
            cancel leash drop
        """})
@Since("1.0.4")
public class CondLeashWillDrop extends Condition {

    static {
        if (Skript.classExists("org.bukkit.event.player.PlayerUnleashEntityEvent")) {
            Skript.registerCondition(CondLeashWillDrop.class, "[the] leash will be dropped",
                    "[the] leash w(ill not|on't) be dropped");
        }
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        if (!(getParser().isCurrentEvent(PlayerUnleashEntityEvent.class))) {
            Skript.error("This condition can only be used in the Unleash Event!");
            return false;
        }
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the leash will " + (isNegated() ? "not" : "") + " be dropped";
    }

    @Override
    public boolean check(@NotNull Event event) {
        return isNegated() ^ ((PlayerUnleashEntityEvent) event).isDropLeash();
    }
}