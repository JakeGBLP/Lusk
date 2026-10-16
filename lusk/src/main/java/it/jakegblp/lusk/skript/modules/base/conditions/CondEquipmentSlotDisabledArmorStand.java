package it.jakegblp.lusk.skript.modules.base.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Armor Stand - is Equipment Slot Disabled")
@Description("Checks if a specific equipment slot of an armorstand is disabled.")
@Examples("if chest slot is disabled for {_armorStand}:")
@Since("1.3")
public class CondEquipmentSlotDisabledArmorStand extends Condition {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.condition(CondEquipmentSlotDisabledArmorStand.class, CondEquipmentSlotDisabledArmorStand::new)
                .patterns(
                        "%equipmentslots% (is|are) disabled (on|for) %livingentities%",
                        "%equipmentslots% (isn't|is not|aren't|are not) disabled (on|for) %livingentities%")
                .register(syntaxRegistry);
    }

    private Expression<EquipmentSlot> equipmentSlotExpression;
    private Expression<LivingEntity> livingEntityExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        equipmentSlotExpression = (Expression<EquipmentSlot>) expressions[0];
        livingEntityExpression = (Expression<LivingEntity>) expressions[1];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event event) {
        return livingEntityExpression.check(event, livingEntity ->
                livingEntity instanceof ArmorStand armorStand
                        && equipmentSlotExpression.check(event, armorStand::isSlotDisabled), isNegated());
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return equipmentSlotExpression.toString(event, debug) + " are "
                + (isNegated() ? "not " : "") + "disabled for " + livingEntityExpression.toString(event, debug);
    }
}