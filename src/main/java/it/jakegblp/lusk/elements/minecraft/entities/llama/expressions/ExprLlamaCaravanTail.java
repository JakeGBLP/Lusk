package it.jakegblp.lusk.elements.minecraft.entities.llama.expressions;

import ch.njol.skript.doc.*;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.PAPER_1_19_2;

@Name("Llama - Caravan Tail")
@Description("Returns the caravan tail of a llama.")
@Examples({"broadcast caravan tail of target"})
@Since("1.0.3")
@RequiredPlugins("Paper 1.19.2+")
@SuppressWarnings("unused")
public class ExprLlamaCaravanTail extends SimplePropertyExpression<LivingEntity, LivingEntity> {
    static {
        if (PAPER_1_19_2)
            register(ExprLlamaCaravanTail.class, LivingEntity.class, "[llama] caravan tail", "livingentities");
    }

    @Override
    public @NotNull Class<? extends LivingEntity> getReturnType() {
        return LivingEntity.class;
    }

    @Override
    public LivingEntity convert(LivingEntity e) {
        if (e instanceof Llama llama) return llama.getCaravanTail();
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "llama caravan tail";
    }
}