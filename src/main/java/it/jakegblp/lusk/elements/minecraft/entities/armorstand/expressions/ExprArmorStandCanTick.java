package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;

import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_PREFIX;

@Name("Armor Stand - can Tick (Property)")
@Description("""
Gets and sets the `canTick` property of an armorstand entity or item.

Unlike other Armorstand properties, this one cannot be used on the armorstand item as of 1.21.3.
""")
@Examples({"set can tick property of target to true", "set can tick state of target to false"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprArmorStandCanTick extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        register(ExprArmorStandCanTick.class, Boolean.class, ARMOR_STAND_PREFIX, "[can] tick", "livingentities");
    }

    @Override
    public Boolean convert(LivingEntity from) {
        if (from instanceof ArmorStand armorStand) {
            return armorStand.canTick();
        }
        return false;
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof ArmorStand armorStand) {
            armorStand.setCanTick(to);
        }
    }

    @Override
    protected String getPropertyName() {
        return "armor stand can tick property";
    }
}