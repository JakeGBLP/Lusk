package me.jake.lusk.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;

@Name("is Left Handed")
@Description("Checks if an entity is left handed")
@Examples({"on damage of player:\n\tif victim is left handed:\n\t\tbroadcast \"A left handed man has been attacked!\""})
@Since("1.0.0")
public class CondLeftHanded extends Condition {

    static {
        Skript.registerCondition(CondLeftHanded.class, "%entity% is[negated:( not|n't)] (:left|right)[ ]handed");
    }

    private Expression<?> entity;

    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parser) {
        entity = expressions[0];
        setNegated(parser.hasTag("negated"));
        if (!parser.hasTag("left")) {
            setNegated(!isNegated());
        }
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "Entity is left handed";
    }

    @Override
    public boolean check(@NotNull Event event) {
        Entity e = (Entity) entity.getSingle(event);
        if (e != null) {
            if (e instanceof Player) {
                if (!isNegated()) {
                    return (Objects.equals(((Player) e).getMainHand().toString(), "LEFT"));
                } else {
                    return !(Objects.equals(((Player) e).getMainHand().toString(), "LEFT"));
                }
            } else if (e instanceof Mob) {
                if (!isNegated()) {
                    return (((Mob)e).isLeftHanded());
                } else {
                    return !(((Mob)e).isLeftHanded());
                }
            }
        }
        return false;
    }
}