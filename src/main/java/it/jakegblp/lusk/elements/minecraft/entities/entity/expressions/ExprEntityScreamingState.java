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
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Goat;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Screaming State")
@Description("Returns the Screaming State of an entity.\n(Enderman, Goat)")
@Examples({"broadcast angry state of target"})
@Since("1.0.2")
public class ExprEntityScreamingState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprEntityScreamingState.class, Boolean.class, ExpressionType.COMBINED,
                "[the] [is] screaming state of %entity%",
                "%entity%'[s] [is] screaming state",
                "whether %entity% is screaming [or not]",
                "whether [or not] %entity% is screaming");
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
        if (livingEntity instanceof Goat goat) bool = goat.isScreaming();
        else if (livingEntity instanceof Enderman enderman) bool = enderman.isScreaming();
        else bool = false;
        return new Boolean[]{bool};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean) {
            LivingEntity livingEntity = livingEntityExpression.getSingle(e);
            if (livingEntity instanceof Goat goat) goat.setScreaming(aBoolean);
            else if (livingEntity instanceof Enderman enderman) enderman.setScreaming(aBoolean);
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
        return "the is screaming state of " + (e == null ? "" : livingEntityExpression.toString(e, debug));
    }
}