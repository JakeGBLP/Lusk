package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.entity.SizedFireball;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Fireball - Item")
@Description("Returns the displayed item of a fireball.\nCan be set.")
@Examples({"broadcast fireball item of target"})
@Since("1.0.3")
public class ExprFireballItem extends SimpleExpression<ItemStack> {
    static {
        Skript.registerExpression(ExprFireballItem.class, ItemStack.class, ExpressionType.COMBINED,
                "[the] [displayed] fireball item of %entity%",
                "%entity%'[s] [displayed] fireball item");
    }

    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected ItemStack @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof SizedFireball fireball) {
            return new ItemStack[]{fireball.getDisplayItem()};
        }
        return new ItemStack[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{ItemStack.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof ItemStack itemStack)
            if (entityExpression.getSingle(e) instanceof SizedFireball fireball) fireball.setDisplayItem(itemStack);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the displayed fireball item of " + (e == null ? "" : entityExpression.toString(e, debug));
    }
}