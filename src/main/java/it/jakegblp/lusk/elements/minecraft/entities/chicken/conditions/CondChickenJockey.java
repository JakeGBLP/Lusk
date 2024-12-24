package it.jakegblp.lusk.elements.minecraft.entities.chicken.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.LivingEntity;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_1_19_2_EXTENDED_ENTITY_API;

@Name("Chicken - is Chicken Jockey")
@Description("Checks if the provided chickens are chicken jockeys.")
@Examples({"if {_chicken} is chicken jockey:"})
@Since("1.3")
@RequiredPlugins("Paper")
public class CondChickenJockey extends PropertyCondition<LivingEntity> {

    static {
        if (PAPER_HAS_1_19_2_EXTENDED_ENTITY_API)
            register(CondChickenJockey.class, "[a] chicken jockey", "livingentities");
    }

    @Override
    public boolean check(LivingEntity value) {
        return value instanceof Chicken chicken && chicken.isChickenJockey();
    }

    @Override
    protected String getPropertyName() {
        return "chicken jockey";
    }
}
