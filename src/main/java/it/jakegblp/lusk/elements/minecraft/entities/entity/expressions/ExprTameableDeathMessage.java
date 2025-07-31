package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.entity.TameableDeathMessageEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;
import static it.jakegblp.lusk.utils.Constants.PAPER_1_18_2;

@Name("Tameable Death Message Event -  Death Message")
@Description("Returns the applied color in the Dye event.\n This expression can be set to another dye color.")
@Examples({"on dye:\n\tbroadcast the entity dye color"})
@Since("1.3.8")
@RequiredPlugins("Paper 1.18.2+")
@SuppressWarnings("unused")
public class ExprTameableDeathMessage extends SimpleExpression<String> {
    static {
        if (PAPER_1_18_2)
            Skript.registerExpression(ExprTameableDeathMessage.class, String.class, EVENT_OR_SIMPLE,
                    "[the |event-]tameable death message");
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(TameableDeathMessageEvent.class)) {
            Skript.error("This expression can only be used in the Tameable Death Message Event!");
            return false;
        }
        return true;
    }

    @Override
    protected String @NotNull [] get(@NotNull Event e) {
        return new String[]{LegacyComponentSerializer.legacySection().serialize(((TameableDeathMessageEvent) e).deathMessage())};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{String[].class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof String string)
            ((TameableDeathMessageEvent) e).deathMessage(LegacyComponentSerializer.legacySection().deserialize(string));
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the tameable death message";
    }
}
