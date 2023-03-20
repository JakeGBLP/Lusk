package me.jake.lusk.elements.experimental.text;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Display Align")
@Description("Aligns a Display Text's text.")
@Examples({""})
@Since("1.0.2")
public class EffDisplayAlign extends Effect {

    static {
        Skript.registerEffect(EffDisplayAlign.class, "align [text of] %entity% to the left", "align [text of] %entity% to the center", "align [text of] %entity% to the right");
    }

    private TextDisplay.TextAligment alignment;

    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        if (matchedPattern == 0) {
            alignment = TextDisplay.TextAligment.LEFT;
        } else if (matchedPattern == 1) {
            alignment = TextDisplay.TextAligment.CENTER;
        } else {
            alignment = TextDisplay.TextAligment.RIGHT;
        }
        entityExpression = (Expression<Entity>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        assert event != null;
        return "align text of " + entityExpression.getSingle(event) + " to the " + alignment.toString().toLowerCase();
    }

    @Override
    protected void execute(@NotNull Event event) {
        Entity entity = entityExpression.getSingle(event);
        assert entity != null;
        TextDisplay display = (TextDisplay)entity;
        display.setAlignment(alignment);
    }
}