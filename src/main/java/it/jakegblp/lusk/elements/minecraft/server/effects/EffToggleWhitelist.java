package it.jakegblp.lusk.elements.minecraft.server.effects;

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
        Skript.registerEffect(EffToggleWhitelist.class, "(:en|dis)able [the] [server] whitelist");
    }

    private boolean enable;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        enable = parser.hasTag("en");
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (enable ? "en" : "dis") + "able the server whitelist";
    }

    @Override
    protected void execute(@NotNull Event event) {
        Bukkit.setWhitelist(enable);
    }
}