package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.IronGolem;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Iron Golem - Created By a Player State")
@Description("Returns whether or not an iron golem was created by a player.\nCan be set.")
@Examples({"broadcast head up state of target"})
@Since("1.0.3")
public class ExprIronGolemCreatedByPlayerState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprIronGolemCreatedByPlayerState.class, Boolean.class, ExpressionType.COMBINED,
                "[the] [iron golem] [was] (created|built) by ([a] player|players) state of %entity%");
    }

    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }
    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof IronGolem ironGolem) {
            return new Boolean[]{ironGolem.isPlayerCreated()};
        }
        return new Boolean[0];
    }
    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Boolean[].class);
        }
        return new Class[0];
    }
    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Boolean aBoolean = delta instanceof Boolean[] ? ((Boolean[]) delta)[0] : null;
        if (aBoolean == null) return;
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof IronGolem ironGolem) {
            ironGolem.setPlayerCreated(aBoolean);
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the iron golem was created by a player state of  " + (e == null ? "" : entityExpression.getSingle(e));
    }
}