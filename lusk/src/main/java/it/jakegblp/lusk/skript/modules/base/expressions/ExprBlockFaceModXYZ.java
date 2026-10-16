package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.Locale;

@Name("BlockFace - Mod XYZ")
@Description("Returns the amount of x/y/z to modify to get the represented block(s).")
@Example("broadcast mod-y of {_blockfaces::*}")
@Since("1.2")
public class ExprBlockFaceModXYZ extends SimplePropertyExpression<BlockFace, Integer> {

    protected Axis axis;

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprBlockFaceModXYZ.class, ExprBlockFaceModXYZ::new, Integer.class)
                .propertyOf("mod(-| )(:x|:y|:z)", "blockfaces")
                .defaultExpression(true)
                .register(syntaxRegistry);
    }

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        axis = Axis.valueOf(parseResult.tags.getFirst().toUpperCase(Locale.ENGLISH));
        return super.init(expressions, matchedPattern, isDelayed, parseResult);
    }

    @Override
    @Nullable
    public Integer convert(BlockFace blockFace) {
        return switch (axis) {
            case X -> blockFace.getModX();
            case Y -> blockFace.getModY();
            case Z -> blockFace.getModZ();
        };
    }

    @Override
    public @NotNull Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "mod-" + axis.name().toLowerCase();
    }
}