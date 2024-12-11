package it.jakegblp.lusk.elements.minecraft.entities.arrow.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.AbstractArrow;
import org.jetbrains.annotations.Nullable;

@Name("Abstract Arrow - Damage")
@Description("Gets the base amount of damage one or more arrows will do.\nDefaults to 2.0 for a normal arrow with 0.5 * (1 + power level) added for arrows fired from enchanted bows.\nCan be set, added to, removed from, deleted (set to 0) and reset (set to 2, the default value).")
@Examples("set arrow damage of {_arrows::*} to 3")
@Since("1.3")
public class ExprArrowDamage extends SimplerPropertyExpression<AbstractArrow, Double> {

    static {
        register(ExprArrowDamage.class, Double.class, "[abstract] arrow damage", "abstractarrows");
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
    public void set(AbstractArrow from, Double to) {
       from.setDamage(to);
    }

    @Override
    public void add(AbstractArrow from, Double to) {
        from.setDamage(from.getDamage()+to);
    }

    @Override
    public void remove(AbstractArrow from, Double to) {
        from.setDamage(from.getDamage()-to);
    }

    @Override
    public void delete(AbstractArrow from) {
        from.setDamage(0);
    }

    @Override
    public void reset(AbstractArrow from) {
        from.setDamage(2);
    }

    @Override
    public @Nullable Double convert(AbstractArrow from) {
        return from.getDamage();
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
