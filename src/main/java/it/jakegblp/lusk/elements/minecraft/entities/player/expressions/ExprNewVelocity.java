package it.jakegblp.lusk.elements.minecraft.entities.player.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;

@Name("on Velocity Change - New Velocity Vector")
@Description("The velocity vector in the Player Velocity Change Event, can be set, deleted and reset.")
@Examples("set the new player velocity to vector(4,6,3)")
@Since("1.3")
public class ExprNewVelocity extends SimpleExpression<Vector> {

    static {
        Skript.registerExpression(ExprNewVelocity.class, Vector.class, EVENT_OR_SIMPLE, "[the] (new|[new] player) velocity [vector]");
    }

    @Override
    protected @Nullable Vector[] get(Event event) {
        PlayerVelocityEvent playerVelocityEvent = (PlayerVelocityEvent) event;
        return new Vector[] {playerVelocityEvent.getVelocity()};
    }

    @Override
    public void change(Event event, @Nullable Object[] delta, Changer.ChangeMode mode) {
        Vector vector = delta == null ? new Vector() :
                (delta.length > 0 ? delta[0] instanceof Vector v ? v : new Vector() : new Vector());
        ((PlayerVelocityEvent) event).setVelocity(vector);
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET -> new Class[] { Vector.class };
            case DELETE, RESET -> new Class[0];
            default -> null;
        };
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Vector> getReturnType() {
        return Vector.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "the new player vector";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        if (!getParser().isCurrentEvent(PlayerVelocityEvent.class)) {
            Skript.error("This expression can only be used in the Player Velocity Change Event!");
            return false;
        }
        return true;
    }
}
