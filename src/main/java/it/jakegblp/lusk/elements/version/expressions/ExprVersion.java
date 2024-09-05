package it.jakegblp.lusk.elements.version.expressions;

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
import com.vdurmont.semver4j.Semver;
import com.vdurmont.semver4j.SemverException;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import static it.jakegblp.lusk.utils.LuskUtils.Version;

@Name("Version")
@Description("Returns a Version")
@Examples({"if player's version < version \"1.19.2\":\n\tbroadcast \"%player% can't play on this server\"\nelse:\n\tkick player"})
@Since("1.0.0")
public class ExprVersion extends SimpleExpression<Semver> {
    static {
        Skript.registerExpression(ExprVersion.class, Semver.class, ExpressionType.COMBINED,
                "[the] version[s] %strings%");
    }

    private Expression<String> strings;

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        strings = (Expression<String>) exprs[0];
        return true;
    }

    @Override
    protected Semver @NotNull [] get(@NotNull Event e) {
        String[] s = strings.getArray(e);
        if (s.length != 0) {
            ArrayList<Semver> v = new ArrayList<>();
            for (String value : s) {
                try {
                    v.add(Version(value));
                } catch (SemverException ignored) {
                }
            }
            return v.toArray(new Semver[0]);
        }
        return new Semver[0];
    }

    @Override
    public boolean isSingle() {
        return strings.isSingle();
    }

    @Override
    public @NotNull Class<? extends Semver> getReturnType() {
        return Semver.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event e, boolean debug) {
        return "the version " + (e == null ? "" : strings.toString(e, debug));
    }
}
