package it.jakegblp.lusk.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Zombie;
import org.jetbrains.annotations.NotNull;

@Name("Entity - Should Burn In Day")
@Description("Checks if an Entity should burn in daylight.\n(Zombie,Phantom,Skeleton)\n\nFor skeletons, this does not take into account the entity's natural fire immunity.")
@Examples({"if target should burn in daylight:"})
@Since("1.0.3")
public class CondEntityBurnDuringDay extends PropertyCondition<LivingEntity> {
    static {
        register(CondEntityBurnDuringDay.class, PropertyType.WILL, "burn during the day", "livingentities");
    }

    @Override
    public boolean check(LivingEntity entity) {
        return  (entity instanceof Zombie zombie && zombie.shouldBurnInDay()) ||
                (entity instanceof AbstractSkeleton skeleton && skeleton.shouldBurnInDay()) ||
                (entity instanceof Phantom phantom && phantom.shouldBurnInDay());
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "burn during the day";
    }
}