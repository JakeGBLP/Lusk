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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Zombie - Should Burn In Day State")
@Description("Returns whether or not a zombie should burn in the day.\nCan be set.")
@Examples({"broadcast raised arms state of target"})
@Since("1.0.3")
public class ExprZombieShouldBurnInDayState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprZombieShouldBurnInDayState.class, Boolean.class, ExpressionType.COMBINED,
                "[the] should burn (in|during) [the] day[light] state of %livingentity%",
                "%livingentity%'[s] should burn (in|during) [the] day[light] state");

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
        if (livingEntity instanceof Zombie zombie) {
            return new Boolean[]{zombie.shouldBurnInDay()};
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
        Boolean bool = delta instanceof Boolean[] ? ((Boolean[]) delta)[0] : null;
        if (bool == null) return;
        LivingEntity livingEntity = livingEntityExpression.getSingle(e);
        if (livingEntity instanceof Zombie zombie) {
            zombie.setShouldBurnInDay(bool);
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
        return "the should burn in the daylight state of " + (e == null ? "" : livingEntityExpression.getSingle(e));
    }
}