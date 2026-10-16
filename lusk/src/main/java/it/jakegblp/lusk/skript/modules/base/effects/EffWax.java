package it.jakegblp.lusk.skript.modules.base.effects;

import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import it.jakegblp.lusk.skript.utils.BlockStateHolder;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;


@Name("Sign - Wax/Unwax")
@Description("Waxed or unwaxes signs, making them uneditable and editable respectively.\nWorks on sign items.")
@Example("wax target block")
@Example("unwax {_b}")
@Example("wax tool")
@Since("2.0.0")
@Keywords({"wax", "waxed", "unwax", "unwaxed", "sign"})
public class EffWax extends Effect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.effect(EffWax.class, EffWax::new)
                .patterns("[:un]wax %blockstateholders%")
                .register(syntaxRegistry);
    }

    private Expression<BlockStateHolder<?>> blockStateHolderExpression;
    private boolean unwax;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        blockStateHolderExpression = (Expression<BlockStateHolder<?>>) expressions[0];
        unwax = parser.hasTag("un");
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return (unwax ? "unw" : "") + "wax " + blockStateHolderExpression.toString(event, debug);
    }

    @Override
    protected void execute(@NotNull Event event) {
        for (BlockStateHolder<?> holder : blockStateHolderExpression.getAll(event))
            holder.editBlockState(Sign.class, sign -> sign.setWaxed(!unwax));
    }
}