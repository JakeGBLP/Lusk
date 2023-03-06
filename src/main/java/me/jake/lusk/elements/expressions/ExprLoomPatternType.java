package me.jake.lusk.elements.expressions;

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
import io.papermc.paper.event.player.PlayerLoomPatternSelectEvent;
import org.bukkit.block.banner.PatternType;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Loom Pattern Type")
@Description("Gets the pattern type selected in a Loom Pattern Select event")
@Examples("""
                            on pattern select of border:
                              set pattern to creeper

                            on pattern select:
                              broadcast event-patterntype

                            on pattern select of bricks:
                              broadcast the selected pattern
                              """)
@Since("1.0.0")
public class ExprLoomPatternType extends SimpleExpression<PatternType> {
    static {
        if (Skript.classExists("io.papermc.paper.event.player.PlayerLoomPatternSelectEvent")) {
            Skript.registerExpression(ExprLoomPatternType.class, PatternType.class, ExpressionType.SIMPLE,
                    "[the] [selected] [loom] [banner] pattern [type]");
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

    @Nullable
    @Override
    public Class<?>[] acceptChange(Changer.@NotNull ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(PatternType[].class);
        }
        return new Class[0];
    }
    @Override
    public void change(@NotNull Event e, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        PatternType patternType = delta instanceof PatternType[] ? ((PatternType[]) delta)[0] : null;
        if (patternType == null) return;
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
        return "the Selected Pattern";
    }
}
