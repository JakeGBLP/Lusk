package it.jakegblp.lusk.elements.minecraft.entities.horse.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

@Name("Horse - is Eating Grass (Property)")
@Description("Returns whether or not an horse is eating grass.\nCan be set and reset.")
@Examples({"broadcast horse eating grass state of target"})
@Since("1.0.3")
@SuppressWarnings("unused")
public class ExprHorseEatingGrassState extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        register(ExprHorseEatingGrassState.class, Boolean.class, "horse", "[is] eating grass", "livingentities");
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof AbstractHorse horse) {
            horse.setEatingGrass(to);
        }
    }

    @Override
    public void reset(LivingEntity from) {
        set(from, false);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public boolean allowReset() {
        return true;
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return from instanceof AbstractHorse horse && horse.isEatingGrass();
    }

    @Override
    protected String getPropertyName() {
        return "horse is eating grass state";
    }
}