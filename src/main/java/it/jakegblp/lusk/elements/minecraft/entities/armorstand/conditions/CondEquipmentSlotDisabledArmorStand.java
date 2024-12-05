package it.jakegblp.lusk.elements.minecraft.entities.armorstand.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

@Name("Armor Stand - is Equipment Slot Disabled")
@Description("Checks if a specific equipment slot of an armorstand is disabled.")
@Examples("if chest slot is disabled for {_armorStand}:")
@Since("1.3")
public class CondEquipmentSlotDisabledArmorStand extends Condition {

    static {
        Skript.registerCondition(CondEquipmentSlotDisabledArmorStand.class,
                "%equipmentslots% (is|are) disabled (on|for) %armorstands%",
                "%equipmentslots% (isn't|is not|aren't|are not) disabled (on|for) %armorstands%");
    }

    private Expression<EquipmentSlot> equipmentSlotExpression;
    private Expression<ArmorStand> armorStandExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        equipmentSlotExpression = (Expression<EquipmentSlot>) expressions[0];
        armorStandExpression = (Expression<ArmorStand>) expressions[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event event) {
        return armorStandExpression.check(event,
                armorStand -> equipmentSlotExpression.check(event, armorStand::isSlotDisabled),isNegated());
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return equipmentSlotExpression.toString(event, debug) + " are "
                + (isNegated() ? "not " : "") + "disabled for "+armorStandExpression.toString(event, debug);
    }
}
