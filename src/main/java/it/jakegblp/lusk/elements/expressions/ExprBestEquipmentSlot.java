package it.jakegblp.lusk.elements.expressions;

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
import ch.njol.util.Kleenean;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Best Equipment Slot")
@Description("""
        Returns the best Slot for an item.
        """)
@Examples({"broadcast best equipment slot for x"})
@Since("1.0.0")
public class ExprBestEquipmentSlot extends SimplePropertyExpression<ItemType, EquipmentSlot> {
    static {
        register(ExprAllayJukebox.class, Block.class, "best equipment slot", "itemtypes");
    }

    @Override
    public @NotNull Class<? extends EquipmentSlot> getReturnType() {
        return EquipmentSlot.class;
    }

    @Override
    @Nullable
    public EquipmentSlot convert(ItemType e) {
        return e.getMaterial().getEquipmentSlot();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "best equipment slot";
    }
}