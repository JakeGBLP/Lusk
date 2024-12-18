package it.jakegblp.lusk.elements.minecraft.mixed.expressions;

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
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Command Block - Command")
@Description("Returns the command within a command block.\nCan be set.\nWorks with minecart with command block.")
@Examples({"broadcast the command of {_cmd}"})
@Since("1.0.3")
@SuppressWarnings("unused")
public class ExprCommandBlockCommand extends SimpleExpression<String> {
    static {
        // TODO: simple PROPERTY EXPR
        Skript.registerExpression(ExprCommandBlockCommand.class, String.class, ExpressionType.PROPERTY,
                "[the] [command block] command of %object%",
                "%object%'[s] [command block] command");
    }

    private Expression<Object> objectExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        objectExpression = (Expression<Object>) exprs[0];
        return true;
    }

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        Object o = objectExpression.getSingle(e);
        if (o instanceof Block block && block.getState() instanceof CommandBlock commandBlock) {
            return new String[]{commandBlock.getCommand()};
        } else if (o instanceof CommandMinecart commandMinecart) {
            return new String[]{commandMinecart.getCommand()};
        }
        return new String[0];
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return switch (mode) {
            case SET -> new Class[]{String.class};
            case DELETE, RESET -> new Class[0];
            case REMOVE, REMOVE_ALL, ADD -> null;
        };
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Object o = objectExpression.getSingle(e);
        if (o instanceof Block block && block.getState() instanceof CommandBlock commandBlock) {
            if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.DELETE) {
                commandBlock.setCommand("");
            } else if (delta[0] instanceof String string) {
                commandBlock.setCommand(string);
            } else {
                return;
            }
            commandBlock.update();
        } else if (o instanceof CommandMinecart commandMinecart) {
            if (mode == Changer.ChangeMode.RESET || mode == Changer.ChangeMode.DELETE) {
                commandMinecart.setCommand("");
            } else if (delta[0] instanceof String string) {
                commandMinecart.setCommand(string);
            }
        }
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
        return "the command block command of " + (e == null ? "" : objectExpression.toString(e, debug));
    }
}
