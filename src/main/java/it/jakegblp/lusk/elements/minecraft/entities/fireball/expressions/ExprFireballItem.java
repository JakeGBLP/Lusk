package it.jakegblp.lusk.elements.minecraft.entities.fireball.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SizedFireball;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Name("Fireball - Displayed Item")
@Description("Gets the displayed item of the provided fireballs.\nCan be set.")
@Examples({"broadcast fireball item of target"})
@Since("1.0.3, 1.3 (Plural)")
public class ExprFireballItem extends SimplerPropertyExpression<Projectile, ItemType> {

    static {
        register(ExprFireballItem.class, ItemType.class, "[displayed] fireball item", "projectiles");
    }

    @Override
    public @Nullable ItemType convert(Projectile from) {
        return from instanceof SizedFireball fireball ? new ItemType(fireball.getDisplayItem()) : null;
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void set(Projectile from, ItemType to) {
        if (from instanceof SizedFireball fireball) {
            ItemStack item = to.getRandom();
            if (item == null) return;
            fireball.setDisplayItem(item);
        }
    }

    @Override
    protected String getPropertyName() {
        return "displayed fireball item";
    }

    @Override
    public Class<? extends ItemType> getReturnType() {
        return ItemType.class;
    }
}