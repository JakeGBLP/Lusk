package it.jakegblp.lusk.elements.expressions;

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
import io.papermc.paper.event.world.border.WorldBorderCenterChangeEvent;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("WorldBorder - New/Old Center")
@Description("Returns the new/old anger of the warden in the World Border Change events.\nCan be set only within the Start variant of the event.")
@Examples({""})
@Since("1.0.2")
public class ExprWorldBorderCenterChangeCenter extends SimpleExpression<Location> {
    static {
        if (Skript.classExists("io.papermc.paper.event.world.border.WorldBorderEvent")) {
            Skript.registerExpression(ExprWorldBorderCenterChangeCenter.class, Location.class, ExpressionType.SIMPLE,
                    "[the] old center",
                    "[the] new center",
                    "[the] center");
        }
    }

    private Boolean past;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(WorldBorderCenterChangeEvent.class)) {
            Skript.error("This expression can only be used in the World Border Change events.!");
            return false;
        }
        if (matchedPattern == 0) {
            past = true;
        } else if (matchedPattern == 1) {
            past = false;
        }
        return true;
    }

    @Override
    protected Location @NotNull [] get(@NotNull Event e) {
        WorldBorderCenterChangeEvent event = (WorldBorderCenterChangeEvent) e;
        Location location;
        if (past) {
            location = event.getOldCenter();
        } else {
            location = event.getNewCenter();
        }
        return new Location[]{location};
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
        ((WorldBorderCenterChangeEvent) e).setNewCenter(location);
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
        return "the" + (past == null ? "" : (past ? " past" : " future")) + " center";
    }
}
