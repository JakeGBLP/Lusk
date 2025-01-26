package it.jakegblp.lusk.elements.minecraft.entities.arrow.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import it.jakegblp.lusk.utils.LuskMath;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Projectile;
import org.jetbrains.annotations.Nullable;

@Name("Arrow - Pierce Amount")
@Description("Sets the number of times this arrow can pierce through an entity. Must be between 0 and 127.")
@Examples("set arrow pierce amount of {_arrows::*} to 3")
@Since("1.3")
public class ExprArrowPierceAmount extends SimplerPropertyExpression<Projectile, Integer> {

    static {
        register(ExprArrowPierceAmount.class, Integer.class, "arrow pierce (amount|level)", "projectiles");
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
    public void set(Projectile from, Integer to) {
        if (from instanceof AbstractArrow abstractArrow) {
            abstractArrow.setPierceLevel(LuskMath.fit(0, to, 127));
        }
    }

    @Override
    public void add(Projectile from, Integer toAdd) {
        if (from instanceof AbstractArrow abstractArrow) {
            abstractArrow.setPierceLevel(LuskMath.fit(0, abstractArrow.getPierceLevel()+toAdd, 127));
        }
    }

    @Override
    public void remove(Projectile from, Integer toRemove) {
        if (from instanceof AbstractArrow abstractArrow) {
            abstractArrow.setPierceLevel(LuskMath.fit(0, abstractArrow.getPierceLevel() - toRemove, 127));
        }
    }

    @Override
    public void delete(Projectile from) {
        set(from, 0);
    }

    @Override
    public void reset(Projectile from) {
        delete(from);
    }

    @Override
    public @Nullable Integer convert(Projectile from) {
        if (from instanceof AbstractArrow abstractArrow) {
            return abstractArrow.getPierceLevel();
        }
        return null;
    }

    @Override
    protected String getPropertyName() {
        return "arrow piece amount";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
}
