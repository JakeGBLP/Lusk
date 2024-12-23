package it.jakegblp.lusk.elements.minecraft.entities.fox.conditions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.PrefixedPropertyCondition;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_1_18_2_EXTENDED_ENTITY_API;

@Name("Fox - is Leaping")
@Description("Checks if a fox is leaping.")
@Examples({"on damage of fox:\n\tif victim is leaping:\n\t\tcancel event"})
@Since("1.0.0")
@RequiredPlugins("Paper 1.18.2")
@SuppressWarnings("unused")
public class CondFoxLeaping extends PrefixedPropertyCondition<LivingEntity> {
    static {
        if (PAPER_HAS_1_18_2_EXTENDED_ENTITY_API)
            register(CondFoxLeaping.class, "[fox[es]]", "leaping", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Fox fox && fox.isLeaping();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "leaping";
    }

    @Override
    public String getPrefix() {
        return "foxes";
    }
}