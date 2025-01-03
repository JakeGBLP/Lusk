package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;

import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_PREFIX;

@Name("Armor Stand - can Move (Property)")
@Description("""
Gets and sets the `canMove` property of an armorstand entity or item.

Unlike other Armorstand properties, this one cannot be used on the armorstand item as of 1.21.3.
""")
@Examples({"set can move property of target to true", "set whether armor stand target can move to true"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprArmorStandCanMove extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        register(ExprArmorStandCanMove.class, Boolean.class, ARMOR_STAND_PREFIX, "[can] move", "livingentities");
    }

    @Override
    public Boolean convert(LivingEntity from) {
        if (from instanceof ArmorStand armorStand) {
            return armorStand.canMove();
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
            armorStand.setCanMove(to);
        }
    }

    @Override
    protected String getPropertyName() {
        return "armor stand can tick property";
    }
}