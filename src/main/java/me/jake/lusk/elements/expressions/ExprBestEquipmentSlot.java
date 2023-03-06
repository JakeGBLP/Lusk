package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.slot.Slot;
import ch.njol.util.Kleenean;
import me.jake.lusk.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Name("Slipperiness")
@Description("""
        Returns a value that represents how 'slippery' the block is. Blocks with higher slipperiness, like ice can be slid on further by the player and other entities.
        Most blocks have a default slipperiness of 0.6.
        
        Only works for placeable item/blocks.
        """)
@Examples({"broadcast best equipment slot for x"})
@Since("1.0.0")
public class ExprBestEquipmentSlot extends SimpleExpression<EquipmentSlot> {
    static {
        Skript.registerExpression(ExprBestEquipmentSlot.class, EquipmentSlot.class, ExpressionType.SIMPLE,
                "[the] [best] [equipment] slot (for|of) %itemtype%",
                "%itemtype%'[s] [best] [equipment] slot");
    }

    private Expression<ItemType> itemTypeExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        itemTypeExpression = (Expression<ItemType>) exprs[0];
        return true;
    }
    @Override
    public boolean isSingle() {
        return true;
    }
    @Override
    protected EquipmentSlot @NotNull [] get(@NotNull Event e) {
        ItemType i = itemTypeExpression.getSingle(e);
        if (i != null) {
            Material material = i.getMaterial();
            EquipmentSlot equipmentSlot = material.getEquipmentSlot();
            return new EquipmentSlot[]{equipmentSlot};
        }
        return new EquipmentSlot[]{};
    }


    @Override
    public @NotNull Class<? extends EquipmentSlot> getReturnType() {
        return EquipmentSlot.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "Best Equipment Slot";
    }
}
