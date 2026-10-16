package it.jakegblp.lusk.skript.modules.base.effects;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.entity.Armadillo;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Armadillo - roll Out/Up")
@Description("Attempts to roll the provided armadillos up (if they're idle) or out (if they're not idle).")
@Example("make target roll up")
@Since("1.3.8")
public class EffArmadilloRoll extends Effect {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.effect(EffArmadilloRoll.class, EffArmadilloRoll::new)
                .patterns(
                        "make [armadillo[s]] %livingentities% roll (up|in)",
                        "make [armadillo[s]] %livingentities% roll out")
                .register(syntaxRegistry);
    }

    private Expression<LivingEntity> entityExpression;
    private boolean out;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        entityExpression = (Expression<LivingEntity>) expressions[0];
        out = matchedPattern == 1;
        return true;
    }

    @Override
    protected void execute(Event event) {
        for (LivingEntity livingEntity : entityExpression.getArray(event)) {
            if (livingEntity instanceof Armadillo armadillo) {
                if (out)
                    armadillo.rollOut();
                else
                    armadillo.rollUp();
            }
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "make armadillos " + entityExpression.toString(event, debug) + " roll " + (out ? "out" : "up");
    }
}