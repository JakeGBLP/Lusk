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
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.EntityUtils.setAngry;

@Name("Entity - Angry State")
@Description("Returns the Angry State of an entity.\n(Warden, PigZombie, Wolf, Enderman)\nCan be set for all except wardens.")
@Examples({"broadcast angry state of target"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprEntityAngryState extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprEntityAngryState.class, Boolean.class, ExpressionType.PROPERTY,
                "[the] ang(ry|er) state of %livingentities%",
                "%livingentities%'[s] ang(ry|er) state",
                "whether %livingentities% is angry [or not]",
                "whether [or not] %livingentities% is angry");
    }

    private Expression<LivingEntity> livingEntityExpression;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed,
                        @NotNull SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) expressions[0];
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        return livingEntityExpression.stream(e)
                .map(EntityUtils::isAngry)
                .toArray(Boolean[]::new);
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean bool) {
            livingEntityExpression.stream(e).forEach(livingEntity -> setAngry(livingEntity,bool));
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
        return "the angry state of " + livingEntityExpression.toString(e, debug);
    }
}