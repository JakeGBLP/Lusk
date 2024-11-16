package it.jakegblp.lusk.elements.minecraft.boundingbox.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
@Name("Bounding Box - of Entity/Block")
@Description("Returns the bounding box of a block or an entity which reflects its location and size. (But not its world, kind of like vectors)")
@Examples({"broadcast bounding box of target"})
@Since("1.0.2, 1.2 (Blocks), 1.3 (BlockStates)")
public class ExprBoundingBoxOf extends SimplePropertyExpression<Object, BoundingBox> {

    static {
        register(ExprBoundingBoxOf.class, BoundingBox.class, "bounding box", "entities/blocks");
    }

    @Override
    public @Nullable BoundingBox convert(Object from) {
        if (from instanceof Entity entity)
            return entity.getBoundingBox();
        else if (from instanceof BlockState blockState)
            return blockState.getBlock().getBoundingBox();
        else if (from instanceof Block block)
            return block.getBoundingBox();
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "bounding box";
    }

    @Override
    public @NotNull Class<? extends BoundingBox> getReturnType() {
        return BoundingBox.class;
    }
}