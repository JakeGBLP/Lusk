package it.jakegblp.lusk.elements.minecraft.entities.hostile.vindicator.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Vindicator;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Name("Vindicator - Johnny State")
@Description("Get and Set whether 1 or more vindicators are Johnny.\nChanging this does not modify the vindicator's name.")
@Examples({"if {_vindicator} is johnny:\n\tset whether vindicator {_vindicator} is johnny to false"})
@Since("1.2")
public class ExprVindicatorJohnny extends PropertyExpression<LivingEntity, Boolean> {

    static {
        Skript.registerExpression(ExprVindicatorJohnny.class, Boolean.class, ExpressionType.PROPERTY,
                "[the] [vindicator] is johnny state of %livingentities%",
                "%livingentities%'[s] [vindicator] is johnny state",
                "whether [the] [vindicator[s]] %livingentities% (is|are) johnny [or not]",
                "whether [or not] [the] [vindicator[s]] %livingentities% (is|are) johnny");
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event event, LivingEntity @NotNull [] source) {
        List<Boolean> booleans = new ArrayList<>();
        for (LivingEntity livingEntity : source) {
            if (livingEntity instanceof Vindicator vindicator) booleans.add(vindicator.isJohnny());
        }
        return booleans.toArray(new Boolean[0]);
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public @Nullable Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) return new Class[0];
        else if (mode == Changer.ChangeMode.SET) {
            return new Class[]{Boolean.class};
        }
        return null;
    }

    @Override
    public void change(@NotNull Event event, @Nullable Object[] delta, Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.DELETE || mode == Changer.ChangeMode.RESET) getExpr().stream(event).forEach(livingEntity -> {
            if (livingEntity instanceof Vindicator vindicator) vindicator.setJohnny(false);
        });
        else if (mode == Changer.ChangeMode.SET) {
            if (delta == null) return;
            if (delta[0] instanceof Boolean b) {
                getExpr().stream(event).forEach(livingEntity -> {
                    if (livingEntity instanceof Vindicator vindicator) vindicator.setJohnny(b);
                });
            }
        }
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "the vindicator is johnny state of "+(event != null ? getExpr().toString(event,debug) : "");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        setExpr((Expression<? extends LivingEntity>) expressions[0]);
        return true;
    }
}
