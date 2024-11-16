package it.jakegblp.lusk.elements.minecraft.entities.player.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Pusher")
@Description("Gets the entity which pushed the affected entity in a Damage Push event")
@Examples("""
        on damage push:
          broadcast the pusher
        """)
@Since("1.0.2")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprPusher extends SimpleExpression<Entity> {
    static {
        if (Skript.classExists("io.papermc.paper.event.entity.EntityPushedByEntityAttackEvent")) {
            Skript.registerExpression(ExprPusher.class, Entity.class, ExpressionType.EVENT,
                    "[the] pusher");
        }
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(EntityPushedByEntityAttackEvent.class)) {
            Skript.error("This expression can only be used in the Damage Push Event!");
            return false;
        }
        return true;
    }

    @Override
    protected Entity @NotNull [] get(@NotNull Event e) {
        return new Entity[]{((EntityPushedByEntityAttackEvent) e).getPushedBy()};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends Entity> getReturnType() {
        return Entity.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the pusher";
    }
}
