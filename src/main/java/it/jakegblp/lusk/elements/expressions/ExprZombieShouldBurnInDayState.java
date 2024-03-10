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
                "[the] zombie should burn (in|during) [the] day[light] state of %livingentity%",
                "%livingentity%'[s] zombie should burn (in|during) [the] day[light] state",
                "whether [the] zombie %livingentity% should burn (in|during) [the] day[light] [or not]",
                "whether [or not] [the] zombie %livingentity% should burn (in|during) [the] day[light]");

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
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean)
            if (livingEntityExpression.getSingle(e) instanceof Zombie zombie)
                zombie.setShouldBurnInDay(aBoolean);

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
        return "the zombie should burn in the daylight state of " + (e == null ? "" : livingEntityExpression.toString(e,debug));
    }
}