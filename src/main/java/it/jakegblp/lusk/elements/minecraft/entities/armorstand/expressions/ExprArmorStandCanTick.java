package it.jakegblp.lusk.elements.minecraft.entities.armorstand.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;

import static ch.njol.skript.paperlib.PaperLib.isPaper;
import static it.jakegblp.lusk.utils.Constants.ARMOR_STAND_PREFIX;

@Name("Armor Stand - can Tick (Property)")
@Description("""
Gets and sets the `canTick` property of an armorstand entity or item.

Unlike other Armorstand properties, this one cannot be used on the armorstand item as of 1.21.3.
""")
@Examples({"set can tick property of target to true", "set whether armor stand target can tick to true"})
@Since("1.0.2")
@RequiredPlugins("Paper")
@SuppressWarnings("unused")
public class ExprArmorStandCanTick extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        if (isPaper())
            register(ExprArmorStandCanTick.class, Boolean.class, ARMOR_STAND_PREFIX, "[can] tick", "livingentities");
    }

    @Override
    public Boolean convert(LivingEntity from) {
        return from instanceof ArmorStand armorStand && armorStand.canTick();
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