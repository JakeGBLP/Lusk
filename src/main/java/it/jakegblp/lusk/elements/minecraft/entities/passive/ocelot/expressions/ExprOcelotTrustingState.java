package it.jakegblp.lusk.elements.minecraft.entities.passive.ocelot.expressions;

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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ocelot;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Ocelot - Trusting State")
@Description("Returns whether or not an ocelot trusts players.\nCan be set.")
@Examples({"broadcast ocelot trusting state of target"})
@Since("1.0.2")
public class ExprOcelotTrustingState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprOcelotTrustingState.class, Boolean.class, ExpressionType.COMBINED,
                "[the] ocelot [is] trust[ing] state of %entity%",
                "%entity%'[s] ocelot [is] trust[ing] state",
                "whether [the] ocelot %entity% (is trusting|trusts players) [or not]",
                "whether [or not] [the] ocelot %entity% (is trusting|trusts players)");
    }

    private Expression<Entity> entityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<Entity>) exprs[0];
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        Entity entity = entityExpression.getSingle(e);
        if (entity instanceof Ocelot ocelot) {
            return new Boolean[]{ocelot.isTrusting()};
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
            if (entityExpression.getSingle(e) instanceof Ocelot ocelot)
                ocelot.setTrusting(aBoolean);
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
        return "the ocelot trusting state of " + (e == null ? "" : entityExpression.toString(e, debug));
    }
}