package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
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
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.Event;
import org.bukkit.inventory.BrewerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Brewing - Fuel")
@Description("Returns the brewing fuel item of a Brewing Stand.\nCan be set.")
@Examples({"on brewing start:\n\tbroadcast the brewing fuel of event-block"})
@Since("1.0.2")
public class ExprBrewingFuel extends SimpleExpression<ItemType> {
    static {
        Skript.registerExpression(ExprBrewingFuel.class, ItemType.class, ExpressionType.COMBINED,
                "[the] brewing fuel of %block%","%block%'[s] brewing fuel");
    }

    private Expression<Block> blockExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        blockExpression = (Expression<Block>) exprs[0];
        return true;
    }

    @Override
    protected ItemType @NotNull [] get(@NotNull Event e) {
        Block block = blockExpression.getSingle(e);
        if (block != null && block.getState() instanceof BrewingStand brewingStand) {
            BrewerInventory brewerInventory = brewingStand.getInventory();
            if (brewerInventory.getFuel() != null) {
                return new ItemType[]{new ItemType(brewerInventory.getFuel())};
            }
        }
        return new ItemType[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{ItemType.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof ItemType itemType) {
            Block block = blockExpression.getSingle(e);
            if (block != null && block.getState() instanceof BrewingStand brewingStand) {
                brewingStand.getInventory().setFuel(itemType.getRandom());
                brewingStand.update();
            }
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the brewing fuel of " + (e == null ? "" : blockExpression.toString(e,debug));
    }
}
