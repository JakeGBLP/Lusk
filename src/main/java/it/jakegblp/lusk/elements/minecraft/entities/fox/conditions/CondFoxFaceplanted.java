package it.jakegblp.lusk.elements.minecraft.entities.fox.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.PrefixedPropertyCondition;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Fox - is Faceplanted")
@Description("Checks if the fox is faceplanted.")
@Examples({"on damage of fox:\n\tif victim is faceplanted:\n\t\tcancel event"})
@Since("1.0.0")
@SuppressWarnings("unused")
public class CondFoxFaceplanted extends PrefixedPropertyCondition<LivingEntity> {
    static {
        register(CondFoxFaceplanted.class, "[fox[es]]", "face[ |-]planted", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return entity instanceof Fox fox && fox.isFaceplanted();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "faceplanted";
    }

    @Override
    public String getPrefix() {
        return "foxes";
    }
}