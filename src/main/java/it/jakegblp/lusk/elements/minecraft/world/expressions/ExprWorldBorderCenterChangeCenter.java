package it.jakegblp.lusk.elements.minecraft.world.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.world.border.WorldBorderCenterChangeEvent;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;
import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_WORLD_BORDER_EVENT;

@Name("Past/Future World Border Center")
@Description("Returns the past/future center of the world border in the World Border Change events.\nThe future center can be set.")
@Examples({""})
@Since("1.0.2")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprWorldBorderCenterChangeCenter extends SimpleExpression<Location> {
    static {
        if (PAPER_HAS_WORLD_BORDER_EVENT) {
            Skript.registerExpression(ExprWorldBorderCenterChangeCenter.class, Location.class, EVENT_OR_SIMPLE,
                    "[the] [future|:past] world[ ]border center");
        }
    }

    private Boolean past;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(WorldBorderCenterChangeEvent.class)) {
            Skript.error("This expression can only be used in the World Border Change events.!");
            return false;
        }
        past = parseResult.hasTag("past");
        return true;
    }

    @Override
    protected Location @NotNull [] get(@NotNull Event e) {
        WorldBorderCenterChangeEvent event = (WorldBorderCenterChangeEvent) e;
        Location location = past ? event.getOldCenter() : event.getNewCenter();
        return new Location[]{location};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Location.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Location location)
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
        return "the" + (past == null ? "" : (past ? " past" : " future")) + " worldborder center";
    }
}
