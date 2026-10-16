package it.jakegblp.lusk.skript.modules.boundingbox.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Bounding Box - Empty")
@Description("Creates a new empty Bounding Box.")
@Example("broadcast a new empty bounding box")
@Since("1.2")
public class ExprBoundingBoxEmpty extends SimpleExpression<BoundingBox> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.expression(ExprBoundingBoxEmpty.class, ExprBoundingBoxEmpty::new, BoundingBox.class)
                .patterns("[a] [new] empty bounding box", "[an] empty bounding box")
                .register(syntaxRegistry);
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    protected BoundingBox @NotNull [] get(@NotNull Event event) {
        return new BoundingBox[]{new BoundingBox()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<BoundingBox> getReturnType() {
        return BoundingBox.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "a new empty bounding box";
    }
}