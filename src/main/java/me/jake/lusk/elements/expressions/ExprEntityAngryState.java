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
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Angry State")
@Description("Returns the Angry State of an entity.\n(Warden, PigZombie, Wolf, Enderman)\nCan be set for all except wardens.")
@Examples({"broadcast angry state of target"})
@Since("1.0.2")
public class ExprEntityAngryState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprEntityAngryState.class, Boolean.class, ExpressionType.COMBINED,
                "[the] angr(y state|iness) of %livingentity%",
                "%livingentity%'[s] angr(y state|iness)");

    }
    private Expression<LivingEntity> livingEntityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) exprs[0];
        return true;
    }
    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        LivingEntity livingEntity = livingEntityExpression.getSingle(e);
        boolean bool;
        if (livingEntity instanceof PigZombie pigZombie) {
            bool = pigZombie.isAngry();
        } else if (livingEntity instanceof Wolf wolf) {
            bool = wolf.isAngry();
        } else if (livingEntity instanceof Warden warden) {
            bool = warden.getAngerLevel() == Warden.AngerLevel.ANGRY;
        } else if (livingEntity instanceof Enderman enderman) {
            bool = enderman.isScreaming();
        } else {
            bool = false;
        }
        return new Boolean[]{bool};
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
        Boolean bool = delta instanceof Boolean[] ? ((Boolean[]) delta)[0] : null;
        if (bool == null) return;
        LivingEntity livingEntity = livingEntityExpression.getSingle(e);
        if (livingEntity instanceof PigZombie pigZombie) {
            pigZombie.setAngry(bool);
        } else if (livingEntity instanceof Wolf wolf) {
            wolf.setAngry(bool);
        } else if (livingEntity instanceof Enderman enderman) {
            enderman.setScreaming(bool);
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
        return "the angry state of " + (e == null ? "" : livingEntityExpression.getSingle(e));
    }
}