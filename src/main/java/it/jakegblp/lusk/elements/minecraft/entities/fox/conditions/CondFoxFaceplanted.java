package it.jakegblp.lusk.elements.minecraft.entities.fox.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.utils.LuskUtils.registerPrefixedPropertyCondition;

@Name("Fox - is Faceplanted")
@Description("Checks if the fox is faceplanted.")
@Examples({"on damage of fox:\n\tif victim is faceplanted:\n\t\tcancel event"})
@Since("1.0.0")
@SuppressWarnings("unused")
public class CondFoxFaceplanted extends PropertyCondition<LivingEntity> {
    static {
        registerPrefixedPropertyCondition(CondFoxFaceplanted.class, "[fox[es]]", "face[ |-]planted", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Fox fox && fox.isFaceplanted();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "faceplanted";
    }
}