package it.jakegblp.lusk.elements.minecraft.blockface.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("BlockFace - Opposite BlockFace")
@Description("Returns the opposite blockface of the provided blockfaces.")
@Examples({"broadcast opposite blockface of north face # south face"})
@Since("1.2")
public class ExprBlockFaceOpposite extends SimplePropertyExpression<BlockFace, BlockFace> {
    static {
        register(ExprBlockFaceOpposite.class, BlockFace.class, "opposite [block[ ]]face", "blockfaces");
    }

    @Override
    public @NotNull Class<? extends BlockFace> getReturnType() {
        return BlockFace.class;
    }

    @Override
    @Nullable
    public BlockFace convert(BlockFace blockFace) {
        return blockFace.getOppositeFace();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "opposite blockface";
    }
}