package it.jakegblp.lusk.elements.minecraft.blocks.brewingstand.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.event.Event;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Name("Brewing - Fuel Item")
@Description("Returns the brewing fuel item of a Brewing Stand.\nCan be set.")
@Examples({"on brewing start:\n\tbroadcast the brewing fuel of event-block"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprBrewingFuel extends SimplePropertyExpression<Block, ItemStack> {

    static {
        register(ExprBrewingFuel.class, ItemStack.class, "brewing fuel", "blocks");
    }

    @Override
    public @Nullable ItemStack convert(Block from) {
        if (from.getState() instanceof BrewingStand brewingStand) {
            BrewerInventory brewerInventory = brewingStand.getInventory();
            if (brewerInventory.getFuel() != null) {
                return brewerInventory.getFuel();
            }
        }
        return null;
    }

    @Override
    protected String getPropertyName() {
        return "brewing fuel";
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        ItemStack fuel = (mode == Changer.ChangeMode.SET && delta != null && delta[0] instanceof ItemStack item) ? item : ItemStack.empty();
        getExpr().stream(event).forEach(block -> {
            if (block.getState() instanceof BrewingStand brewingStand) {
                BrewerInventory brewerInventory = brewingStand.getInventory();
                brewerInventory.setFuel(fuel);
                brewingStand.update();
            }
        });

    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> new Class<?>[] { ItemStack.class };
            case DELETE, RESET -> new Class<?>[0];
            default -> null;
        };
    }
}
