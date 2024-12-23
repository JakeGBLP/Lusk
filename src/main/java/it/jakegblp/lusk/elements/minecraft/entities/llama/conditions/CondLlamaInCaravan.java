package it.jakegblp.lusk.elements.minecraft.entities.llama.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_1_19_2_EXTENDED_ENTITY_API;

@Name("Llama - is In Caravan")
@Description("Checks if a llama is in a caravan.")
@Examples({"if target is in a caravan:"})
@Since("1.0.3, 1.3 (Plural)")
@RequiredPlugins("Paper 1.19.2+")
@SuppressWarnings("unused")
public class CondLlamaInCaravan extends PropertyCondition<LivingEntity> {
    static {
        if (PAPER_HAS_1_19_2_EXTENDED_ENTITY_API)
            register(CondLlamaInCaravan.class, "in [a] caravan", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Llama llama && llama.inCaravan();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "in a caravan";
    }
}