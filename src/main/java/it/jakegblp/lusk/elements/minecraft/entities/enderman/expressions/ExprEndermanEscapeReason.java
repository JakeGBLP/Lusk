package it.jakegblp.lusk.elements.minecraft.entities.enderman.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.event.entity.EndermanEscapeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;

@Name("Enderman - Escape Reason")
@Description("Returns the Escape Reason in an Enderman Escape Event.\nThis Expression requires Paper.")
@Examples({"on enderman escape:\n\tbroadcast event-enderman escape reason"})
@Since("1.0.0")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprEndermanEscapeReason extends SimpleExpression<EndermanEscapeEvent.Reason> {
    static {
        if (Skript.classExists("com.destroystokyo.paper.event.entity.EndermanEscapeEvent")) {
            Skript.registerExpression(ExprEndermanEscapeReason.class, EndermanEscapeEvent.Reason.class, EVENT_OR_SIMPLE,
                    "[the] enderman escape reason");
        }
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(EndermanEscapeEvent.class)) {
            Skript.error("This expression can only be used in the Enderman Escape Event!");
            return false;
        }
        return true;
    }

    @Override
    protected EndermanEscapeEvent.Reason @NotNull [] get(@NotNull Event e) {
        return new EndermanEscapeEvent.Reason[]{((EndermanEscapeEvent) e).getReason()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends EndermanEscapeEvent.Reason> getReturnType() {
        return EndermanEscapeEvent.Reason.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the enderman escape reason";
    }
}
