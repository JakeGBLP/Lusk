package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import it.jakegblp.lusk.api.skript.PropertyExpression;
import it.jakegblp.lusk.utils.EntityUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Name("Entity - Item from Equipment Slot")
@Description("""
        Gets the item in one or more equipment slots of one or more entities, if the entity cannot use a provided slot it will fail silently.
        This might not work when trying to get a slot on an entity that doesnt have that slot AND if you're not running at least Paper 1.21
        
        Can be set.""")
@Examples({"broadcast equipment slot chest slot"})
@Since("1.3")
@SuppressWarnings("unused")
public class ExprEntityEquipmentSlot extends PropertyExpression<LivingEntity, ItemType> {
    static {
        register(ExprEntityEquipmentSlot.class, ItemType.class,
                "equipment[[ |-]slot][s] %equipmentslots%",
                "livingentities");
    }

    Expression<EquipmentSlot> equipmentSlotExpression;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parseResult) {
        if (matchedPattern == 0) {
            equipmentSlotExpression = (Expression<EquipmentSlot>) expressions[0];
            setExpr((Expression<? extends LivingEntity>) expressions[1]);
        } else {
            equipmentSlotExpression = (Expression<EquipmentSlot>) expressions[1];
            setExpr((Expression<? extends LivingEntity>) expressions[0]);
        }
        return true;
    }

    @Override
    public boolean isSingle() {
        return super.isSingle() && equipmentSlotExpression.isSingle();
    }

    @Override
    protected ItemType @NotNull [] get(@NotNull Event event, LivingEntity @NotNull [] source) {
        return getExpr().stream(event)
                .flatMap(entity -> equipmentSlotExpression.stream(event)
                        .map(equipmentSlot -> EntityUtils.getEntityEquipmentSlot(entity, equipmentSlot))
                        .filter(Objects::nonNull)
                        .map(ItemType::new))
                .toArray(ItemType[]::new);
    }

    @Override
    public Class<?>[] acceptChange(final Changer.@NotNull ChangeMode mode) {
        return mode == Changer.ChangeMode.SET ? new Class[]{ItemType.class} : mode == Changer.ChangeMode.DELETE ? new Class[0] : null;
    }

    @Override
    public void change(@NotNull Event event, Object @NotNull [] delta, Changer.@NotNull ChangeMode mode) {
        ItemStack itemStack;
        if (mode == Changer.ChangeMode.SET && delta.length > 0 && delta[0] instanceof ItemType itemType) {
            itemStack = itemType.getRandom();
        } else {
            itemStack = null;
        }
        getExpr().stream(event)
                .forEach(entity -> equipmentSlotExpression.stream(event)
                        .forEach(equipmentSlot -> EntityUtils.setEntityEquipmentSlot(entity,equipmentSlot,itemStack)));
    }

    @Override
    public @NotNull Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "equipment slots " + equipmentSlotExpression.toString(event,debug) + " of "+getExpr().toString(event,debug);
    }
}