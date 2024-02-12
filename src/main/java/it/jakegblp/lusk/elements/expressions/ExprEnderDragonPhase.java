package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Name("Ender Dragon Phase")
@Description("Returns the Ender Dragon phase in an Ender Dragon Phase Change Event.\nCan be set.")
@Examples({"on ender dragon phase change:\n\tbroadcast the phase"})
@Since("1.0.2")
public class ExprEnderDragonPhase extends SimpleExpression<EnderDragon.Phase> {
    static {
        Skript.registerExpression(ExprEnderDragonPhase.class, EnderDragon.Phase.class, ExpressionType.SIMPLE,
                "[the] old [[ender[ ]]dragon] phase",
                "[the] new [[ender[ ]]dragon] phase",
                "[the] [[ender[ ]]dragon] phase");
    }

    private Boolean past = null;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(EnderDragonChangePhaseEvent.class)) {
            Skript.error("This expression can only be used in the Ender Dragon Phase Change Event!");
            return false;
        }
        if (matchedPattern == 0) {
            past = true;
        } else if (matchedPattern == 1) {
            past = false;
        }
        return true;
    }

    @Override
    protected EnderDragon.Phase @NotNull [] get(@NotNull Event e) {
        EnderDragon.Phase phase;

        if (past == null || !past) {
            phase = ((EnderDragonChangePhaseEvent) e).getNewPhase();
        } else {
            phase = ((EnderDragonChangePhaseEvent) e).getCurrentPhase();
        }
        return new EnderDragon.Phase[]{phase};
    }

    @Override
    public Class<?> @NotNull [] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(EnderDragon.Phase[].class);
        }
        return new Class[0];
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        EnderDragon.Phase phase = delta instanceof EnderDragon.Phase[] ? ((EnderDragon.Phase[]) delta)[0] : null;
        if (phase == null) return;
        ((EnderDragonChangePhaseEvent) e).setNewPhase(phase);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends EnderDragon.Phase> getReturnType() {
        return EnderDragon.Phase.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the" + (past == null ? "" : (past ? " past" : " future")) + " ender dragon phase";
    }
}
