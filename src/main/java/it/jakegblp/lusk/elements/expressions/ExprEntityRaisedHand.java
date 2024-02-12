package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Raised Hand")
@Description("Return the hand raised by this entity.\nEither HAND or OFF_HAND.\nIf the entity is not currently raising its hand, this will return the last raised hand.")
@Examples({"broadcast raised hand of target"})
@Since("1.0.3")
public class ExprEntityRaisedHand extends SimplePropertyExpression<Entity, EquipmentSlot> {
    static {
        register(ExprEntityRaisedHand.class, EquipmentSlot.class, "raised hand", "entity");
    }

    @Override
    public @NotNull Class<? extends EquipmentSlot> getReturnType() {
        return EquipmentSlot.class;
    }

    @Override
    @Nullable
    public EquipmentSlot convert(Entity e) {
        if (e instanceof LivingEntity livingEntity) {
            return livingEntity.getHandRaised();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "raised hand";
    }
}