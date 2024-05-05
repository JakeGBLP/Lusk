package it.jakegblp.lusk.elements.minecraft.entities.neutral.fox.expressions;

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
                "[the] fox [is] (1:leaping|2:crouching|3:defending|face[ ]planted) state of %livingentity%",
                "%livingentity%'[s] fox [is] (:leaping|:crouching|:defending|face[ ]planted) state",
                "whether [the] fox %livingentity% is (:leaping|:crouching|:defending|face[ ]planted) [or not]",
                "whether [or not] [the] fox %livingentity% is (:leaping|:crouching|:defending|face[ ]planted)");

    }

    private Expression<LivingEntity> livingEntityExpression;

    private int mark;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        livingEntityExpression = (Expression<LivingEntity>) exprs[0];
        mark = parseResult.mark;
        return true;
    }

    @Override
    protected Boolean @NotNull [] get(@NotNull Event e) {
        if (livingEntityExpression.getSingle(e) instanceof Fox fox) {
            boolean bool = switch (mark) {
                case 1 -> fox.isLeaping();
                case 2 -> fox.isCrouching();
                case 3 -> fox.isDefending();
                default -> fox.isFaceplanted();
            };
            return new Boolean[]{bool};
        }
        return new Boolean[]{false};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{Boolean.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof Boolean bool) {
            if (livingEntityExpression.getSingle(e) instanceof Fox fox) {
                switch (mark) {
                    case 1 -> fox.setLeaping(bool);
                    case 2 -> fox.setCrouching(bool);
                    case 3 -> fox.setDefending(bool);
                    default -> fox.setFaceplanted(bool);
                }
            }
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
        String state = switch (mark) {
            case 1 -> "leaping";
            case 2 -> "crouching";
            case 3 -> "defending";
            default -> "face planted";
        };
        return "the fox is " + state + " state of " + (e == null ? "" : livingEntityExpression.toString(e, debug));
    }
}