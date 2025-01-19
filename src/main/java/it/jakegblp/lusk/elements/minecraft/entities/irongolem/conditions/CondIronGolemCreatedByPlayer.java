package it.jakegblp.lusk.elements.minecraft.entities.irongolem.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.ExtendedPropertyType;
import it.jakegblp.lusk.api.skript.PrefixedPropertyCondition;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

@Name("Iron Golem - Created By a Player")
@Description("Checks if an iron golem was created by a player.")
@Examples({"if {_golem} was created by a player:"})
@Since("1.0.3")
@SuppressWarnings("unused")
public class CondIronGolemCreatedByPlayer extends PrefixedPropertyCondition<LivingEntity> {
    static {
        register(CondIronGolemCreatedByPlayer.class, ExtendedPropertyType.WAS, "iron[ |-]golem[s]", "(created|built) by ([a] player|players)", "livingentities");
    }

    @Override
    public boolean check(LivingEntity value) {
        return value instanceof IronGolem ironGolem && ironGolem.isPlayerCreated();
    }

    @Override
    public @Nullable String getPrefix() {
        return "iron golems";
    }


    @Override
    protected String getPropertyName() {
        return "created by a player";
    }
}