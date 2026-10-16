package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Block - Blast Resistance")
@Description("""
        Gets the blast resistance value (also known as block "durability").
        This value is used in explosions to calculate whether a block should be broken or not.
        
        Only works for placeable item/blocks.
        """)
@Example("broadcast blast resistance of obsidian")
@Since("1.0.0")
public class ExprBlockBlastResistance extends SimplePropertyExpression<Object, Float> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprBlockBlastResistance.class, ExprBlockBlastResistance::new, Float.class)
                .propertyOf("[block] blast resistance", "itemtypes/blocks/blockdatas/blockstates")
                .register(syntaxRegistry);
    }

    @Override
    public @NotNull Class<? extends Float> getReturnType() {
        return Float.class;
    }

    @Override
    @Nullable
    public Float convert(Object object) {
        return switch (object) {
            case ItemType itemType -> itemType.hasBlockType() ? itemType.getBlockType().getBlastResistance() : null;
            case Block block -> block.getType().getBlastResistance();
            case BlockData blockData -> blockData.getMaterial().getBlastResistance();
            case BlockState blockState -> blockState.getType().getBlastResistance();
            default -> null;
        };
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "block blast resistance";
    }
}