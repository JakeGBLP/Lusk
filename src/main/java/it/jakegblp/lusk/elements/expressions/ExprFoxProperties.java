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
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Fox - Properties")
@Description("Various fox properties, can be set.")
@Examples({"broadcast interested state of target"})
@Since("1.0.2")
public class ExprFoxProperties extends SimpleExpression<Boolean> {
    static {
        Skript.registerExpression(ExprFoxProperties.class, Boolean.class, ExpressionType.COMBINED,
                "[the] [fox] leaping state of %livingentity%",
                "%livingentity%'[s] [fox] leaping state",
                "[the] [fox] crouching state of %livingentity%",
                "%livingentity%'[s] [fox] crouching state",
                "[the] [fox] defending state of %livingentity%",
                "%livingentity%'[s] [fox] defending state",
                "[the] [fox] face[ ]planted state of %livingentity%",
                "%livingentity%'[s] [fox] face[ ]planted state");

    }

    private Expression<LivingEntity> livingEntityExpression;

    private int pattern;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) exprs[0];
        pattern = matchedPattern;
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        LivingEntity livingEntity = livingEntityExpression.getSingle(e);
        if (!(livingEntity instanceof Fox fox)) return new Boolean[]{false};
        boolean bool = switch (pattern) {
            case 0, 1 -> fox.isLeaping();
            case 2, 3 -> fox.isCrouching();
            case 4, 5 -> fox.isDefending();
            case 6, 7 -> fox.isFaceplanted();
            default -> false;
        };
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
        LivingEntity livingEntity = livingEntityExpression.getSingle(e);
        if (!(livingEntity instanceof Fox fox)) return;
        boolean bool = Boolean.TRUE.equals(delta instanceof Boolean[] ? ((Boolean[]) delta)[0] : null);
        switch (pattern) {
            case 0, 1 -> fox.setLeaping(bool);
            case 2, 3 -> fox.setCrouching(bool);
            case 4, 5 -> fox.setDefending(bool);
            case 6, 7 -> fox.setFaceplanted(bool);
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
        String state = switch (pattern) {
            case 0, 1 -> "leaping";
            case 2, 3 -> "crouching";
            case 4, 5 -> "defending";
            case 6, 7 -> "face planted";
            default -> "";
        };
        return "the " + state + " state of " + (e == null ? "" : livingEntityExpression.getSingle(e));
    }
}