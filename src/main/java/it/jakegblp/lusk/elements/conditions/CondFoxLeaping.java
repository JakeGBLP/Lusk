package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Fox - is Leaping")
@Description("Checks if a fox is leaping.")
@Examples({"on damage of fox:\n\tif victim is leaping:\n\t\tcancel event"})
@Since("1.0.0")
public class CondFoxLeaping extends PropertyCondition<LivingEntity> {
    static {
        register(CondFoxLeaping.class, "leaping", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity instanceof Fox fox) {
            return fox.isLeaping();
        }
        return false;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "leaping";
    }
}