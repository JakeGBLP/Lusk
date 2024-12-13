package it.jakegblp.lusk.elements.minecraft.entities.arrow.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Projectile;
import org.jetbrains.annotations.Nullable;

@Name("Abstract Arrow - Damage")
@Description("Gets the base amount of damage one or more arrows will do.\nDefaults to 2.0 for a normal arrow with 0.5 * (1 + power level) added for arrows fired from enchanted bows.\nCan be set, added to, removed from, deleted (set to 0) and reset (set to 2, the default value).")
@Examples("set arrow damage of {_arrows::*} to 3")
@Since("1.3")
public class ExprArrowDamage extends SimplerPropertyExpression<Projectile, Double> {

    static {
        register(ExprArrowDamage.class, Double.class, "[abstract] arrow damage", "projectiles");
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowAdd() {
        return true;
    }

    @Override
    public boolean allowRemove() {
        return true;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return true;
    }

    @Override
    public void set(Projectile from, Double to) {
        if (from instanceof AbstractArrow abstractArrow) {
            abstractArrow.setDamage(to);
        }
    }

    @Override
    public void add(Projectile from, Double toAdd) {
        if (from instanceof AbstractArrow abstractArrow) {
            abstractArrow.setDamage(abstractArrow.getDamage()+toAdd);
        }
    }

    @Override
    public void remove(Projectile from, Double toRemove) {
        if (from instanceof AbstractArrow abstractArrow) {
            abstractArrow.setDamage(abstractArrow.getDamage() - toRemove);
        }
    }

    @Override
    public void delete(Projectile from) {
        set(from, 0d);
    }

    @Override
    public void reset(Projectile from) {
        set(from, 2d);
    }

    @Override
    public @Nullable Double convert(Projectile from) {
        if (from instanceof AbstractArrow abstractArrow) {
            return abstractArrow.getDamage();
        }
        return null;
    }

    @Override
    protected String getPropertyName() {
        return "abstract arrow damage";
    }

    @Override
    public Class<? extends Double> getReturnType() {
        return Double.class;
    }
}
