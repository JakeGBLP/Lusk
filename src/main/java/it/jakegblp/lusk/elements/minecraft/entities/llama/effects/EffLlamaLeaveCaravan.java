package it.jakegblp.lusk.elements.minecraft.entities.llama.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@Name("Llama - Leave Caravan")
@Description("Makes a llama leave its caravan.")
@Examples(" make target leave its caravan")
@Since("1.0.3")
@SuppressWarnings("unused")
public class EffLlamaLeaveCaravan extends Effect {
    static {
        Skript.registerEffect(EffLlamaLeaveCaravan.class,
                "make %livingentities% leave [its|the] caravan");
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
        return "make " + (event == null ? "" : entityExpression.getArray(event)) + "leave caravan";
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