package it.jakegblp.lusk.elements.minecraft.blocks.loom.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import io.papermc.paper.event.player.PlayerLoomPatternSelectEvent;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.EVENT_OR_SIMPLE;

@Name("Loom Pattern Type")
@Description("Gets the pattern type selected in a Loom Pattern Select event")
@Examples("""
        on pattern select of border:
          set the loom pattern to creeper

        on pattern select:
          broadcast loom pattern

        on pattern select of bricks:
          broadcast the selected loom pattern
        """)
@Since("1.0.0")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprLoomPatternType extends SimpleExpression<PatternType> {
    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerLoomPatternSelectEvent")) {
            Skript.registerExpression(ExprLoomPatternType.class, PatternType.class, EVENT_OR_SIMPLE,
                    "[the] [selected] loom [banner] pattern [type]");
        }
    }

    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull ParseResult parseResult) {
        if (!getParser().isCurrentEvent(PlayerLoomPatternSelectEvent.class)) {
            Skript.error("This expression can only be used in the Loom Pattern Select Event!");
            return false;
        }
        return true;
    }

    @Override
    protected PatternType @NotNull [] get(@NotNull Event e) {
        return new PatternType[]{((PlayerLoomPatternSelectEvent) e).getPatternType()};
    }

    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{PatternType.class} : null;
    }

    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        if (delta[0] instanceof PatternType patternType)
            ((PlayerLoomPatternSelectEvent) e).setPatternType(patternType);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends PatternType> getReturnType() {
        return PatternType.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the selected loom banner pattern type";
    }
}
