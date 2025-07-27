package it.jakegblp.lusk.elements.minecraft.entities.armadillo.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.Armadillo;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("Armadillo - roll Out/Up")
@Description("""
        Attempts to roll the provided armadillos up (if they're idle) or out (if they're not idle).
        """)
@Examples("make target roll up")
@Since("1.3.8")
@RequiredPlugins("1.21.5")
public class EffArmadilloRoll extends Effect {

    static {
        Skript.registerEffect(EffArmadilloRoll.class,
                "make [armadillo[s]] %livingentities% roll (up|in)",
                "make [armadillo[s]] %livingentities% roll out"
        );
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
        for (LivingEntity livingEntity : entityExpression.getAll(event)) {
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
