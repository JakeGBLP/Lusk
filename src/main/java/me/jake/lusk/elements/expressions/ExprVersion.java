package me.jake.lusk.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.jake.lusk.classes.Version;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

@Name("Version")
@Description("Returns a Version")
@Examples({"if player's version < version \"1.19.2\":\n\tbroadcast \"%player% can't play on this server\"\nelse:\n\tkick player"})
@Since("1.0.0")
public class ExprVersion extends SimpleExpression<Version> {
    static {
        Skript.registerExpression(ExprVersion.class, Version.class, ExpressionType.COMBINED,
                "[the] version[s] %strings%");
    }

    private Expression<String> strings;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        strings = (Expression<String>) exprs[0];
        return true;
    }
    @Override
    protected Version @NotNull [] get(@NotNull Event e) {
        String[] s = strings.getArray(e);
        if (s.length != 0) {
            ArrayList<Version> v = new ArrayList<>();
            for (String value : s) {
                if (Version.parse(value) != null) {
                    v.add(Version.parse(value));
                }
            }
            return v.toArray(new Version[0]);
        }
        return new Version[0];
    }

    @Override
    public boolean isSingle() {
        return strings.isSingle();
    }

    @Override
    public @NotNull Class<? extends Version> getReturnType() {
        return Version.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        assert e != null;
        return "the version " + strings.getSingle(e);
    }
}
