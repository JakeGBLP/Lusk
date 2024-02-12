package it.jakegblp.lusk.elements.expressions;

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
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Leash Holder")
@Description("Returns the Leash Holder of an entity.\nCan be set when using the first 2 patterns.")
@Examples({"broadcast angry state of target"})
@Since("1.0.4")
public class ExprEntityLeashHolder extends SimpleExpression<Entity> {
    static {
        Skript.registerExpression(ExprEntityLeashHolder.class, Entity.class, ExpressionType.COMBINED,
                "[the] leash holder of %livingentity%",
                "%livingentity%'[s] leash holder",
                "[the] leash holder");

    }

    private Expression<LivingEntity> livingEntityExpression;
    private boolean event;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        event = (matchedPattern == 2);
        if (!event) {
            livingEntityExpression = (Expression<LivingEntity>) exprs[0];
        } else return getParser().isCurrentEvent(PlayerLeashEntityEvent.class);
        return true;
    }

    @Override
    protected Entity @NotNull [] get(@NotNull Event e) {
        if (!event) {
            LivingEntity livingEntity = livingEntityExpression.getSingle(e);
            if (livingEntity != null) {
                return new Entity[]{livingEntity.getLeashHolder()};
            }
        } else if (e instanceof PlayerLeashEntityEvent leashEntityEvent) {
            return new Entity[]{leashEntityEvent.getLeashHolder()};
        }
        return new Entity[0];
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(Entity[].class);
        }
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (event) return;
        Entity entity = delta instanceof Entity[] ? ((Entity[]) delta)[0] : null;
        if (entity == null) return;
        LivingEntity livingEntity = livingEntityExpression.getSingle(e);
        if (livingEntity == null) return;
        livingEntity.setLeashHolder(entity);
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
        return (event ? "the leash holder" : "the leash holder of " + (e == null ? "" : livingEntityExpression.getSingle(e)));
    }
}