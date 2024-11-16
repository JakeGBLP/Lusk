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
@SuppressWarnings("unused")
public class ExprEntityScreamingState extends SimpleExpression<Boolean> {
    static {
        // todo: make plural, make util method?
        Skript.registerExpression(ExprEntityScreamingState.class, Boolean.class, ExpressionType.PROPERTY,
                "[the] [is] screaming state of %livingentities%",
                "%entity%'[s] [is] screaming state",
                "whether %livingentities% is screaming [or not]",
                "whether [or not] %livingentities% is screaming");
    }

    private Expression<LivingEntity> livingEntityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) exprs[0];
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        return (Boolean[]) livingEntityExpression.stream(e).map(entity -> {
            if (entity instanceof Goat goat) {
                return goat.isScreaming();
            } else if (entity instanceof Enderman enderman) {
                return enderman.isScreaming();
            }
            return false;
        }).toArray();
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean aBoolean) {
            livingEntityExpression.stream(e).forEach(entity -> {
                if (entity instanceof Goat goat) {
                    goat.setScreaming(aBoolean);
                } else if (entity instanceof Enderman enderman) {
                    enderman.setScreaming(aBoolean);
                }
            });
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
        return "the is screaming state of " + livingEntityExpression.toString(e, debug);
    }
}