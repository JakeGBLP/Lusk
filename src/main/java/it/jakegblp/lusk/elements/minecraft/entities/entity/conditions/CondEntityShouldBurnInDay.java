package it.jakegblp.lusk.elements.minecraft.entities.entity.conditions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.ExtendedPropertyType;
import it.jakegblp.lusk.api.skript.PrefixedPropertyCondition;
import org.bukkit.entity.LivingEntity;

import static it.jakegblp.lusk.utils.EntityUtils.shouldBurnDuringTheDay;

@Name("Entity - Should Burn In Day")
@Description("Checks if an Entity should burn in daylight.\n(Zombie,Phantom,Skeleton)\n\nFor skeletons, this does not take into account the entity's natural fire immunity.")
@Examples({"if target should burn in daylight:"})
@Since("1.0.3, 1.1.1 (Skeleton,Phantom)")
@DocumentationId("11181")
public class CondEntityShouldBurnInDay extends PrefixedPropertyCondition<LivingEntity> {

    static {
        register(CondEntityShouldBurnInDay.class, ExtendedPropertyType.SHOULD, "[[living[ |-]]entity]",
                "burn ((during|in) [the] day|(in|under) [the] (sun[light]|daylight))","livingentities");
    }

    @Override
    public boolean check(LivingEntity value) {
        return shouldBurnDuringTheDay(value);
    }

    @Override
    protected String getPropertyName() {
        return "burn in the daylight";
    }

    @Override
    public String getPrefix() {
        return "living entity";
    }
}