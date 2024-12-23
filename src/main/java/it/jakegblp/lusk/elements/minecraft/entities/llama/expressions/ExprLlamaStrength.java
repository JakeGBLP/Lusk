package it.jakegblp.lusk.elements.minecraft.entities.llama.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.jetbrains.annotations.Nullable;

@Name("Llama - Strength")
@Description("""
Returns the provided llamas' strength.
A higher strength llama will have more inventory slots and be more threatening to entities.

Can be set.

Must be between 1 and 5.""")
@Examples({"broadcast strength of target"})
@Since("1.0.3, 1.3 (Plural)")
@SuppressWarnings("unused")
public class ExprLlamaStrength extends SimplerPropertyExpression<LivingEntity,Integer> {

    static {
        register(ExprLlamaStrength.class,Integer.class,"llama strength", "livingentities");
    }

    @Override
    public @Nullable Integer convert(LivingEntity from) {
        return from instanceof Llama llama ? llama.getStrength() : null;
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
    public void set(LivingEntity from, Integer to) {
        if (from instanceof Llama llama) {
            llama.setStrength(Math.clamp(to,1,5));
        }
    }

    @Override
    public void add(LivingEntity from, Integer to) {
        if (from instanceof Llama llama) {
            llama.setStrength(Math.clamp(llama.getStrength()+to,1,5));
        }
    }

    @Override
    public void remove(LivingEntity from, Integer to) {
        if (from instanceof Llama llama) {
            llama.setStrength(Math.clamp(llama.getStrength()-to,1,5));
        }
    }

    @Override
    protected String getPropertyName() {
        return "llama strength";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
}