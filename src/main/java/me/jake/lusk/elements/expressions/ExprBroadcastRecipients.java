package me.jake.lusk.elements.expressions;

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
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Broadcast Recipients")
@Description("Returns the recipients in the Broadcast event.")
@Examples({"on broadcast:\n\tbroadcast the broadcast recipients"})
@Since("1.0.2")
public class ExprBroadcastRecipients extends SimpleExpression<CommandSender> {
    static {
        Skript.registerExpression(ExprBroadcastRecipients.class, CommandSender.class, ExpressionType.SIMPLE,
                "[the] broadcast recipients");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(BroadcastMessageEvent.class)) {
            Skript.error("This expression can only be used in the Broadcast event!");
            return false;
        }
        return true;
    }
    @Override
    protected CommandSender @NotNull [] get(@NotNull Event e) {
        return ((BroadcastMessageEvent) e).getRecipients().toArray(new CommandSender[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public @NotNull Class<? extends CommandSender> getReturnType() {
        return CommandSender.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the broadcast recipients";
    }
}
