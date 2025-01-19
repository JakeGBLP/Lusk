package it.jakegblp.lusk.elements.minecraft.boundingbox.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.DeprecationUtils.test;

@Name("Bounding Box - Overlaps")
@Description("Whether or not one or more bounding boxes overlap other bounding boxes.\nBounding boxes that are only intersecting at the borders are not considered overlapping.")
@Examples({"if {_box} overlaps bounding box of player:"})
@Since("1.3")
public class CondBoundingBoxOverlaps extends Condition {
    static {
        Skript.registerCondition(CondBoundingBoxOverlaps.class,
                "%boundingboxes% overlap[s] [with] [[bounding[ ]]box] %boundingboxes%",
                "%boundingboxes% do[es](n't| not) overlap [with] [[bounding[ ]]box] %boundingboxes%"
        );
    }

    private Expression<BoundingBox> boundingBox1;
    private Expression<BoundingBox> boundingBox2;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(
            Expression<?>[] expressions,
            int matchedPattern,
            @NotNull Kleenean isDelayed,
            SkriptParser.@NotNull ParseResult parseResult) {
        boundingBox1 = (Expression<BoundingBox>) expressions[0];
        boundingBox2 = (Expression<BoundingBox>) expressions[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(@NotNull Event event) {
        return test(boundingBox1, event, box1 -> test(boundingBox2, event, box1::overlaps, BoundingBox.class), BoundingBox.class, isNegated());
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return boundingBox1.toString(event, debug) + (isNegated() ? " don't overlap " : " overlap ") + boundingBox2.toString(event, debug);
    }
}
