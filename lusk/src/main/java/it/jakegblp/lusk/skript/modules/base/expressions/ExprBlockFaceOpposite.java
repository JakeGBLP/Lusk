package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("BlockFace - Opposite BlockFace")
@Description("Returns the opposite blockface of the provided blockfaces.")
@Example("broadcast opposite blockface of north face # south face")
@Since("1.2")
public class ExprBlockFaceOpposite extends SimplePropertyExpression<BlockFace, BlockFace> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprBlockFaceOpposite.class, ExprBlockFaceOpposite::new, BlockFace.class)
                .propertyOf("opposite [block[ ]]face", "blockfaces")
                .defaultExpression(true)
                .register(syntaxRegistry);
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