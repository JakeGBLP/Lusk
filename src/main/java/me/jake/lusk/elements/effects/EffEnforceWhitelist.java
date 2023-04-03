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

@Name("Enforce Whitelist")
@Description("Sets if the whitelist is enforced.")
@Examples({"enforce the whitelist"})
@Since("1.0.2")
public class EffEnforceWhitelist extends Effect {
    static {
        Skript.registerEffect(EffEnforceWhitelist.class, "enforce [the] [server] whitelist","unenforce [the] [server] whitelist");
    }

    private boolean negated;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        negated = matchedPattern == 1;
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (negated ? "unenforce" : "enforce") + " the server whitelist";
    }

    @Override
    protected void execute(@NotNull Event event) {
        Bukkit.setWhitelistEnforced(!negated);
    }
}