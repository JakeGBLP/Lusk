package it.jakegblp.lusk.elements.minecraft.boundingbox.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.block.Block;
import org.bukkit.util.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;


@Name("Voxel Shape - of Block")
@Description("Returns the detailed collision shape of a block.")
@Examples({"broadcast voxel shape of target block"})
@Since("1.2")
public class ExprVoxelShapeOf extends SimplePropertyExpression<Block, VoxelShape> {

    static {
        register(ExprVoxelShapeOf.class, VoxelShape.class, "(voxel|collision) shape", "blocks");
    }

    @Override
    public @Nullable VoxelShape convert(Block from) {
        return from.getCollisionShape();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "voxel shape";
    }

    @Override
    public @NotNull Class<? extends VoxelShape> getReturnType() {
        return VoxelShape.class;
    }
}
