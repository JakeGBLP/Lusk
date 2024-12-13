package it.jakegblp.lusk.elements.minecraft.entities.fox.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_FOX_API;
import static it.jakegblp.lusk.utils.LuskUtils.registerPrefixedPropertyCondition;

@Name("Fox - is Leaping")
@Description("Checks if a fox is leaping.")
@Examples({"on damage of fox:\n\tif victim is leaping:\n\t\tcancel event"})
@Since("1.0.0")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class CondFoxLeaping extends PropertyCondition<LivingEntity> {
    static {
        if (PAPER_HAS_FOX_API)
            registerPrefixedPropertyCondition(CondFoxLeaping.class, "[fox[es]]", "leaping", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Fox fox && fox.isLeaping();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "leaping";
    }
}