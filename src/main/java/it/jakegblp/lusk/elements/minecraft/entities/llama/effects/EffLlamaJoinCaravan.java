package it.jakegblp.lusk.elements.minecraft.entities.llama.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.*;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_19_2;

@Name("Llama - Join Caravan")
@Description("Makes a llama join another llama's caravan.")
@Examples("make target join caravan of {llama}")
@Since("1.0.3")
@RequiredPlugins("Paper 1.19.2+")
@SuppressWarnings("unused")
public class EffLlamaJoinCaravan extends Effect {
    static {
        if (PAPER_1_19_2)
            Skript.registerEffect(EffLlamaJoinCaravan.class,
                    "make %livingentities% join [the] caravan of %livingentity%",
                    "make %livingentities% join %livingentity%'[s] caravan");
    }

    private Expression<LivingEntity> entityExpression;
    private Expression<LivingEntity> entityExpression1;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        entityExpression = (Expression<LivingEntity>) expressions[0];
        entityExpression1 = (Expression<LivingEntity>) expressions[1];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + entityExpression.toString(event, debug) + " join the caravan of " + entityExpression1.toString(event, debug);
    }

    @Override
    protected void execute(@NotNull Event event) {
        LivingEntity entity1 = entityExpression1.getSingle(event);
        if (!(entity1 instanceof Llama llama1)) return;
        LivingEntity[] entities = entityExpression.getArray(event);
        for (LivingEntity entity : entities) {
            if (entity instanceof Llama llama) {
                llama.joinCaravan(llama1);
            }
        }
    }
}