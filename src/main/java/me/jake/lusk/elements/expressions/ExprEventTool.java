package me.jake.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Used Hand/Item")
@Description("Returns the item used in an event, slot is included.")
@Examples({""})
@Since("1.0.4")
public class ExprEventTool extends SimplePropertyExpression<LivingEntity, ItemStack> {
    static {
        register(ExprEventTool.class, ItemStack.class, "active item", "livingentity");
    }

    @Override
    public @NotNull Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    @Nullable
    public ItemStack convert(LivingEntity e) {
        if (e != null) {
            return e.getActiveItem();
        }
        return null;
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "active item";
    }
}