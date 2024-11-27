package it.jakegblp.lusk.elements.minecraft.blocks.brewingstand.expressions;

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
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.Event;
import org.bukkit.inventory.BrewerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Brewing - Ingredient")
@Description("Returns the brewing ingredient of a Brewing Stand.\nCan be set.")
@Examples({"on brewing start:\n\tbroadcast the brewing ingredient of event-block"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprBrewingIngredient extends SimpleExpression<ItemType> {
    static {
        Skript.registerExpression(ExprBrewingIngredient.class, ItemType.class, ExpressionType.PROPERTY,
                "[the] brewing ingredient of %block%",
                "%block%'[s] brewing ingredient");
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
        if (block != null) {
            BlockState blockState = block.getState();
            if (blockState instanceof BrewingStand brewingStand) {
                BrewerInventory brewerInventory = brewingStand.getInventory();
                if (brewerInventory.getIngredient() != null) {
                    return new ItemType[]{new ItemType(brewerInventory.getIngredient())};
                }
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
                BrewerInventory brewerInventory = brewingStand.getInventory();
                brewerInventory.setIngredient(itemType.getRandom());
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
        return "the brewing ingredient of " + blockExpression.toString(e, debug);
    }
}
