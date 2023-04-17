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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Last Death Location")
@Description("Returns the location in which the player has last died.")
@Examples({"broadcast last death location of target"})
@Since("1.0.2")
public class ExprLastDeathLocation extends SimpleExpression<Location> {
    static {
        Skript.registerExpression(ExprLastDeathLocation.class, Location.class, ExpressionType.COMBINED,
                "[the] last death location of %player%",
                "%player%'[s] last death location");

    }
    private Expression<Player> playerExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        playerExpression = (Expression<Player>) exprs[0];
        return true;
    }
    @Override
    protected Location @NotNull [] get(@NotNull Event e) {
        Player p = playerExpression.getSingle(e);
        if (p != null) {
            return new Location[]{p.getLastDeathLocation()};
        }
        return new Location[0];
    }
    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Location[].class);
        }
        return new Class[0];
    }
    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        Location location = delta instanceof Location[] ? ((Location[]) delta)[0] : null;
        if (location == null) return;
        Player p = playerExpression.getSingle(e);
        if (p != null) {
            p.setLastDeathLocation(location);
        }
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the last death location of " + (e == null ? "" : playerExpression.getSingle(e));
    }
}