package it.jakegblp.lusk.elements.minecraft.entities.other.itemframe.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.utils.Constants;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Item Frame - Rotation")
@Description("Returns the rotation of an Item Frame in Degrees.\nCan be set to the following values:\n0, 45, 90, 135, 180, 225, 270, 315")
@Examples({"broadcast item frame rotation of target"})
@Since("1.0.2")
public class ExprItemFrameRotation extends SimpleExpression<Integer> {
    static {
        Skript.registerExpression(ExprItemFrameRotation.class, Integer.class, ExpressionType.COMBINED,
                "[the] item[ ]frame rotation of %entity%",
                "%entity%'[s] item[ ]frame rotation");
    }

    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected Integer @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof ItemFrame itemFrame) {
            Object rawRotationNumber = Constants.itemFrameRotations.get(itemFrame.getRotation());
            if (rawRotationNumber != null) {
                return new Integer[]{(int) rawRotationNumber};
            }
        }
        return new Integer[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Integer.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Integer integer && Constants.itemFrameRotations.containsValue(integer))
            if (entityExpression.getSingle(e) instanceof ItemFrame itemFrame)
                itemFrame.setRotation(Constants.itemFrameRotations.getKey(integer));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the itemframe rotation of " + (e == null ? "" : entityExpression.toString(e, debug));
    }
}
