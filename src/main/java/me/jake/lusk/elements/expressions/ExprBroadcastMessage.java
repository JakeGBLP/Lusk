package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.event.Event;
import org.bukkit.event.server.BroadcastMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Broadcast Message")
@Description("Returns the message in the Broadcast event.\nCan be set.")
@Examples({"on broadcast:\n\tbroadcast the broadcast message"})
@Since("1.0.2")
public class ExprBroadcastMessage extends SimpleExpression<String> {
    static {
        Skript.registerExpression(ExprBroadcastMessage.class, String.class, ExpressionType.SIMPLE,
                "[the] broadcast message");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(BroadcastMessageEvent.class)) {
            Skript.error("This expression can only be used in the Broadcast event!");
            return false;
        }
        return true;
    }
    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        return new String[]{((TextComponent)((BroadcastMessageEvent)e).message()).content()};
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String[].class);
        } else {
            return new Class[0];
        }
    }
    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        String string = delta instanceof String[] ? ((String[]) delta)[0] : null;
        if (string == null) return;
        ((BroadcastMessageEvent)e).message(Component.text(string));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the broadcast message";
    }
}
