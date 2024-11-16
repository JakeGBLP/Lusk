package it.jakegblp.lusk.elements.minecraft.entities.llama.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.jetbrains.annotations.NotNull;

@Name("Llama - Has Caravan Tail")
@Description("Checks if another llama is currently following behind this llama.")
@Examples({"on damage:\n\tif victim has a caravan tail:\n\t\tbroadcast \"the caravan is being disturbed!!\""})
@Since("1.0.3")
@SuppressWarnings("unused")
public class CondLlamaHasCaravanTail extends PropertyCondition<LivingEntity> {
    static {
        register(CondLlamaHasCaravanTail.class, PropertyType.HAVE, "[a] caravan tail", "livingentities");
    }

    @Override
    public boolean check(LivingEntity e) {
        return e instanceof Llama llama && llama.hasCaravanTail();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "a caravan tail";
    }

}