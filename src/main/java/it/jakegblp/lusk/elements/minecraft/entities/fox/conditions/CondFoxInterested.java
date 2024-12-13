package it.jakegblp.lusk.elements.minecraft.entities.fox.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.*;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.Constants.PAPER_HAS_FOX_API;
import static it.jakegblp.lusk.utils.LuskUtils.registerPrefixedPropertyCondition;

@Name("Fox - is Interested")
@Description("Checks if a fox is interested.")
@Examples({"on damage of fox:\n\tif victim is interested:\n\t\tcancel event"})
@Since("1.3")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class CondFoxInterested extends PropertyCondition<LivingEntity> {
    static {
        if (PAPER_HAS_FOX_API)
            registerPrefixedPropertyCondition(CondFoxInterested.class, "[fox[es]]", "interested", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Fox fox && fox.isInterested();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "interested";
    }
}