package it.jakegblp.lusk.elements.minecraft.entities.enderdragon.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;

@Name("Ender Dragon Phase")
@Description("Returns the Ender Dragon phase in an Ender Dragon Phase Change Event.\nCan be set. (Setting will always change the NEW phase)")
@Examples({"on ender dragon phase change:\n\tbroadcast the current ender dragon phase"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprEnderDragonPhase extends SimpleExpression<EnderDragon.Phase> {
    static {
        Skript.registerExpression(ExprEnderDragonPhase.class, EnderDragon.Phase.class, EVENT_OR_SIMPLE,
                "(the |event-)[new|:current] ender[ ]dragon phase");
    }

    private boolean current;

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(EnderDragonChangePhaseEvent.class)) {
            Skript.error("This expression can only be used in the Ender Dragon Phase Change Event!");
            return false;
        }
        current = parseResult.hasTag("current");
        return true;
    }

    @Override
    protected EnderDragon.Phase @NotNull [] get(@NotNull Event e) {
        EnderDragonChangePhaseEvent event = (EnderDragonChangePhaseEvent) e;
        EnderDragon.Phase phase = current ? event.getCurrentPhase() : event.getNewPhase();
        return new EnderDragon.Phase[]{phase};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{EnderDragon.Phase.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof EnderDragon.Phase phase)
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
        return "the " + (current ? "current" : "new") + " ender dragon phase";
    }
}
