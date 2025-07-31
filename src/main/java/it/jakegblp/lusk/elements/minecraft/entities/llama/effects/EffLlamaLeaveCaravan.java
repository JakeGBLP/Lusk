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

@Name("Llama - Leave Caravan")
@Description("Makes a llama leave its caravan.")
@Examples("make target leave its caravan")
@Since("1.0.3")
@RequiredPlugins("Paper 1.19.2+")
@SuppressWarnings("unused")
public class EffLlamaLeaveCaravan extends Effect {
    static {
        if (PAPER_1_19_2)
            Skript.registerEffect(EffLlamaLeaveCaravan.class,
                    "make %livingentities% leave [its|the[ir]] caravan");
    }

    private Expression<LivingEntity> entityExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        entityExpression = (Expression<LivingEntity>) expressions[0];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "make " + entityExpression.toString(event,debug) + "leave caravan";
    }

    @Override
    protected void execute(@NotNull Event event) {
        LivingEntity[] entities = entityExpression.getArray(event);
        for (LivingEntity entity : entities) {
            if (entity instanceof Llama llama) {
                llama.leaveCaravan();
            }
        }
    }
}