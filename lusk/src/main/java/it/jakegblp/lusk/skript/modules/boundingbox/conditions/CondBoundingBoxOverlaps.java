package it.jakegblp.lusk.skript.modules.boundingbox.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Bounding Box - Overlaps")
@Description("Whether one or more bounding boxes overlap other bounding boxes.\nBounding boxes that are only intersecting at the borders are not considered overlapping.")
@Example("if {_box} overlaps with bounding box of player:")
@Since({"1.3", "2.0.0 (Mandatory \"with\")"})
public class CondBoundingBoxOverlaps extends Condition {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.condition(CondBoundingBoxOverlaps.class, CondBoundingBoxOverlaps::new)
                .patterns("%boundingboxes% overlap[s] with %boundingboxes%",
                        "%boundingboxes% do[es](n't| not) overlap with %boundingboxes%")
                .register(syntaxRegistry);
    }

    private Expression<BoundingBox> boundingBoxExpression, overlapsWithExpression;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        boundingBoxExpression = (Expression<BoundingBox>) expressions[0];
        overlapsWithExpression = (Expression<BoundingBox>) expressions[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(@NotNull Event event) {
        return boundingBoxExpression.check(event, box -> overlapsWithExpression.check(event, box::overlaps), isNegated());
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return boundingBoxExpression.toString(event, debug) + (isNegated() ? " don't" : "") + " overlap " + overlapsWithExpression.toString(event, debug);
    }
}