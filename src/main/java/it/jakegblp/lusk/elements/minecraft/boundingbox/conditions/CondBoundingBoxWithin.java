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
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Bounding Box - X is Within")
@Description("Whether or not a Vector, Location, or Bounding Box is within a Bounding Box.\n\nVector and Location = Checks if the Bounding Box contains a specified position.\nBounding Box = Checks if the Bounding Box fully contains a Bounding Box.")
@Examples({"if location of player is within bounding box of player:"})
@Since("1.2")
public class CondBoundingBoxWithin extends Condition {
    static {
        Skript.registerCondition(CondBoundingBoxWithin.class,
                "%vectors/locations/boundingboxes% (is|are) (within|in[side [of]]) [[bounding[ ]]box] %boundingbox%",
                "%vectors/locations/boundingboxes% (isn't|is not|aren't|are not) (within|in[side [of]]) [[bounding[ ]]box] %boundingbox%"
        );
    }

    private Expression<Object> objects;
    private Expression<BoundingBox> boundingBox;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(
            Expression<?>[] expressions,
            int matchedPattern,
            @NotNull Kleenean isDelayed,
            SkriptParser.@NotNull ParseResult parseResult) {
        objects = (Expression<Object>) expressions[0];
        boundingBox = (Expression<BoundingBox>) expressions[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(@NotNull Event event) {
        return objects.check(event, object ->
                boundingBox.check(event, b -> {
                    if (object instanceof Vector vector) {
                        return b.contains(vector);
                    } else if (object instanceof Location location) {
                        return b.contains(location.toVector());
                    } else if (object instanceof BoundingBox box) {
                        return b.contains(box);
                    } else {
                        return false;
                    }
                }), isNegated());
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        String box = "";
        String object = "";
        if (event != null) {
            box = boundingBox.toString(event, debug);
            object = objects.toString(event, debug);
        }
        return object + " is " + (isNegated() ? "not" : "") + " within " + box;
    }
}
