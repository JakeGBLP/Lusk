package it.jakegblp.lusk.elements.other.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Do Nothing...")
@Description("Does nothing.\nUseful as a placeholder.")
@Examples({"pass\ndo nothing"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class EffPass extends Effect {
    static {
        Skript.registerEffect(EffPass.class, "pass", "do nothing");
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "pass";
    }

    @Override
    protected void execute(@NotNull Event event) {
    }
}