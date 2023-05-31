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
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Toggle Whitelist")
@Description("Sets if the server is whitelisted.")
@Examples({"enable whitelist"})
@Since("1.0.2")
public class EffToggleWhitelist extends Effect {
    static {
        Skript.registerEffect(EffToggleWhitelist.class, "enable [the] [server] whitelist", "disable [the] [server] whitelist");
    }

    private boolean negated;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        negated = matchedPattern == 1;
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (negated ? "disable" : "enable") + " the server whitelist";
    }

    @Override
    protected void execute(@NotNull Event event) {
        Bukkit.setWhitelist(!negated);
    }
}