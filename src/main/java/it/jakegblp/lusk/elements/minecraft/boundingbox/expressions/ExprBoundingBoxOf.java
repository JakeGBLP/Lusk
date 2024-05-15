package it.jakegblp.lusk.elements.minecraft.boundingbox.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Bounding Box - of Entity/Block")
@Description("Returns the bounding box of a block or an entity which reflects its location and size. (But not its world, kind of like vectors)")
@Examples({"broadcast bounding box of target"})
@Since("1.0.2, 1.2 (Blocks)")
public class ExprBoundingBoxOf extends PropertyExpression<Object, BoundingBox> {
    static {
        Skript.registerExpression(ExprBoundingBoxOf.class, BoundingBox.class, ExpressionType.PROPERTY,
                "[the] bounding box of %entities/blocks%",
                "%entities/blocks%'[s] bounding box");
    }

    @Override
    public boolean init(final Expression<?>[] vars, final int matchedPattern, final Kleenean isDelayed, final SkriptParser.ParseResult parser) {
        setExpr(vars[0]);
        return true;
    }


    @Override
    public @NotNull Class<BoundingBox> getReturnType() {
        return BoundingBox.class;
    }

    @Override
    public boolean isSingle() {
        return getExpr().isSingle();
    }

    @Override
    protected BoundingBox[] get(Event e, Object[] source) {
        return get(source, o -> {
            if (o instanceof Entity entity) return entity.getBoundingBox();
            else if (o instanceof Block block) return block.getBoundingBox();
            return null;
        });
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the bounding box of " + (event != null ? getExpr().toString(event, debug) : "");
    }
}