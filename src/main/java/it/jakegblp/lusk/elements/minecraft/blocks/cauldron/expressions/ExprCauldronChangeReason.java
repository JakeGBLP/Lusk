package it.jakegblp.lusk.elements.minecraft.blocks.cauldron.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;

@Name("Cauldron - Change Reason")
@Description("Returns the Change Reason in a Cauldron Level Change Event.")
@Examples({"on cauldron level change:\n\tbroadcast the cauldron change reason"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprCauldronChangeReason extends SimpleExpression<CauldronLevelChangeEvent.ChangeReason> {
    static {
        Skript.registerExpression(ExprCauldronChangeReason.class, CauldronLevelChangeEvent.ChangeReason.class, EVENT_OR_SIMPLE,
                "(the |event-)cauldron change reason");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(CauldronLevelChangeEvent.class)) {
            Skript.error("This expression can only be used in the Cauldron Level Change Event!");
            return false;
        }
        return true;
    }

    @Override
    protected CauldronLevelChangeEvent.ChangeReason @NotNull [] get(@NotNull Event e) {
        return new CauldronLevelChangeEvent.ChangeReason[]{((CauldronLevelChangeEvent) e).getReason()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends CauldronLevelChangeEvent.ChangeReason> getReturnType() {
        return CauldronLevelChangeEvent.ChangeReason.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the cauldron change reason";
    }
}
