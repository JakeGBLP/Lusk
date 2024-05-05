package it.jakegblp.lusk.elements.minecraft.entities.hostile.enderman.events.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.destroystokyo.paper.event.entity.EndermanEscapeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Enderman - Escape Reason")
@Description("Returns the Escape Reason in an Enderman Escape Event.\nThis Expression requires Paper.")
@Examples({"on enderman escape:\n\tbroadcast event-enderman escape reason"})
@Since("1.0.0")
public class ExprEndermanEscapeReason extends SimpleExpression<EndermanEscapeEvent.Reason> {
    static {
        if (Skript.classExists("com.destroystokyo.paper.event.entity.EndermanEscapeEvent")) {
            Skript.registerExpression(ExprEndermanEscapeReason.class, EndermanEscapeEvent.Reason.class, ExpressionType.SIMPLE,
                    "(the |event-)enderman escape reason");
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
