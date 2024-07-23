package it.jakegblp.lusk.elements.minecraft.blockface.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("BlockFace - Vector")
@Description("Returns the vector corresponding to the provided blockfaces.")
@Examples({"broadcast vector direction of west face"})
@Since("1.2")
public class ExprBlockFaceVector extends SimplePropertyExpression<BlockFace, Vector> {
    static {
        register(ExprBlockFaceVector.class, Vector.class, "vector [direction]", "blockfaces");
    }

    @Override
    public @NotNull Class<? extends Vector> getReturnType() {
        return Vector.class;
    }

    @Override
    @Nullable
    public Vector convert(BlockFace blockFace) {
        return blockFace.getDirection();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "vector direction";
    }
}