package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.entity.EntityDyeEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;

@Name("Dye Event - Dye Color")
@Description("Returns the applied color in the Dye event.\n This expression can be set to another dye color.")
@Examples({"on dye:\n\tbroadcast the entity dye color"})
@Since("1.0.0")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprDyeColor extends SimpleExpression<SkriptColor> {
    static {
        Skript.registerExpression(ExprDyeColor.class, SkriptColor.class, EVENT_OR_SIMPLE,
                "[the |event-]entity dye color");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(EntityDyeEvent.class)) {
            Skript.error("This expression can only be used in the Dye Event!");
            return false;
        }
        return true;
    }

    @Override
    protected SkriptColor @NotNull [] get(@NotNull Event e) {
        return new SkriptColor[]{SkriptColor.fromDyeColor(((EntityDyeEvent) e).getColor())};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{SkriptColor[].class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof SkriptColor color)
            // todo: null check
            ((EntityDyeEvent) e).setColor(color.asDyeColor());
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends SkriptColor> getReturnType() {
        return SkriptColor.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the entity dye color";
    }
}
