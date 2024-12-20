package it.jakegblp.lusk.elements.minecraft.entities.fox.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_FOX_API;
import static it.jakegblp.lusk.utils.LuskUtils.registerPrefixedPropertyCondition;

@Name("Fox - is Defending")
@Description("Checks if the fox is defending.")
@Examples({"on damage of fox:\n\tif victim is defending:\n\t\tcancel event"})
@Since("1.0.0")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class CondFoxDefending extends PropertyCondition<LivingEntity> {
    static {
        if (PAPER_HAS_FOX_API)
            registerPrefixedPropertyCondition(CondFoxDefending.class, "[fox[es]]", "defending", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Fox fox && fox.isDefending();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "defending";
    }
}