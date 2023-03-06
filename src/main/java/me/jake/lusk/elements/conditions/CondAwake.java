package me.jake.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("is Awake")
@Description("Checks the current waking state of this bat.")
@Examples({"on damage of bat:\n\tcancel event if victim is not awake"})
@Since("1.0.0")
public class CondAwake extends PropertyCondition<LivingEntity> {

    static {
        register(CondAwake.class, "awake", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity.getType() == EntityType.BAT) {
            Bat bat = (Bat) entity;
            return bat.isAwake();
        } else {
            return !entity.isSleeping();
        }
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "awake";
    }

}