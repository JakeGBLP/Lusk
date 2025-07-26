package it.jakegblp.lusk.elements.minecraft.entities.horse.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

@Name("Horse - is Eating (Property)")
@Description("Returns whether or not an horse is eating.\nCan be set and reset.")
@Examples({"broadcast horse eating state of target"})
@Since("1.0.3")
@SuppressWarnings("unused")
public class ExprHorseEatingState extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        register(ExprHorseEatingState.class, Boolean.class, "horse", "[is] eating", "livingentities");
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof AbstractHorse horse) {
            horse.setEating(to);
        }
    }

    @Override
    public void reset(LivingEntity from) {
        set(from, false);
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return from instanceof AbstractHorse horse && horse.isEating();
    }

    @Override
    protected String getPropertyName() {
        return "horse is eating state";
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return true;
    }
}