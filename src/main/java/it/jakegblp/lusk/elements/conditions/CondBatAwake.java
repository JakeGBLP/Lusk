package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.Bat;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@Name("Entity - is Awake")
@Description("Checks the current waking state of a living entity.")
@Examples({"on damage of bat:\n\tcancel event if victim is not awake"})
@Since("1.0.0")
public class CondBatAwake extends PropertyCondition<LivingEntity> {

    static {
        register(CondBatAwake.class, "awake", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        if (entity instanceof Bat bat) {
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