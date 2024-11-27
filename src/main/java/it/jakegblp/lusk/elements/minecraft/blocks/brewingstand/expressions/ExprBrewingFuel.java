package it.jakegblp.lusk.elements.minecraft.blocks.brewingstand.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.BlockUtils.getBrewingFuel;
import static it.jakegblp.lusk.utils.BlockUtils.setBrewingFuel;

@Name("BrewingStand - Fuel Item")
@Description("Returns the brewing fuel item of a Brewing Stand.\nCan be set.")
@Examples({"on brewing start:\n\tbroadcast the brewing fuel of event-block"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprBrewingFuel extends SimplePropertyExpression<Block, ItemType> {

    static {
        register(ExprBrewingFuel.class, ItemType.class, "brewing fuel [item]", "blocks");
    }

    @Override
    public @Nullable ItemType convert(Block from) {
        return getBrewingFuel(from);
    }

    @Override
    protected String getPropertyName() {
        return "brewing fuel item";
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> new Class[] { ItemStack.class };
            case DELETE, RESET -> new Class[0];
            default -> null;
        };
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        ItemType fuel = (mode == Changer.ChangeMode.SET && delta != null && delta[0] instanceof ItemType item) ? item : null;
        getExpr().stream(event).forEach(block -> setBrewingFuel(block,fuel));
    }
}
