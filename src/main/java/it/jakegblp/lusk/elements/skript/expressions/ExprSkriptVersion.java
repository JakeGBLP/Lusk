package it.jakegblp.lusk.elements.skript.expressions;

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
import it.jakegblp.lusk.utils.Constants;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Skript - Version")
@Description("The skript version.")
@Examples("send the skript version")
@Since("1.3.6")
public class ExprSkriptVersion extends SimpleExpression<Semver> {

    static {
        Skript.registerExpression(ExprSkriptVersion.class, Semver.class, ExpressionType.SIMPLE, "[the] skript version");
    }

    @Override
    protected @Nullable Semver[] get(Event event) {
        return new Semver[]{Constants.VERSION_SKRIPT};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Semver> getReturnType() {
        return Semver.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "skript version";
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        return true;
    }
}
