package it.jakegblp.lusk.elements.minecraft.blockface.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("BlockFace - Mod XYZ")
@Description("Returns the amount of x/y/z to modify to get the represented block(s).")
@Examples({"broadcast mod-y of {_blockfaces::*}"})
@Since("1.2")
public class ExprBlockFaceModXYZ extends SimplePropertyExpression<BlockFace, Integer> {
    static {
        register(ExprBlockFaceModXYZ.class, Integer.class, "mod(-| )(:y|:x|z)", "blockfaces");
    }

    int which;

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        which = parseResult.hasTag("y") ? 0 : (parseResult.hasTag("x") ? 1 : 2);
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    @Nullable
    public Integer convert(BlockFace blockFace) {
        return which == 0 ? blockFace.getModX() : (which == 1 ? blockFace.getModY() : blockFace.getModZ());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "opposite blockface";
    }
}