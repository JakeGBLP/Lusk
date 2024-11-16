package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

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
import it.jakegblp.lusk.utils.EntityUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Should Burn In Day State")
@Description("Returns whether or not an entity should burn in the day.\nCan be set.")
@Examples({"broadcast whether target should burn in day"})
@Since("1.0.3, 1.1.1 (Skeleton,Phantom)")
@SuppressWarnings("unused")
public class ExprEntityShouldBurnInDayState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprEntityShouldBurnInDayState.class, Boolean.class, ExpressionType.PROPERTY,
                "[the] should burn (in|during) [the] day[light] state of %livingentities%",
                "%livingentities%'[s] should burn (in|during) [the] day[light] state",
                "whether %livingentities% should burn (in|during) [the] day[light] [or not]",
                "whether [or not] %livingentities% should burn (in|during) [the] day[light]");
    }

    private Expression<LivingEntity> livingEntityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) exprs[0];
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        for (LivingEntity entity : livingEntityExpression.getAll(e)) {
            if (!EntityUtils.shouldBurnDuringTheDay(entity)) return new Boolean[]{false};
        }
        return new Boolean[]{true};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean) {
            EntityUtils.setShouldBurnDuringTheDay(aBoolean, livingEntityExpression.getAll(e));
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
        return "the should burn in the daylight state of " + livingEntityExpression.toString(e, debug);
    }
}