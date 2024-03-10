package it.jakegblp.lusk.elements.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Big Fall Damage Sound")
@Description("Returns the Sound this entity will make when falling from a large height.")
@Examples({"broadcast big fall damage sound of target"})
@Since("1.0.4")
public class ExprEntityBigFallDamageSound extends SimplePropertyExpression<LivingEntity, String> {
    static {
        register(ExprEntityBigFallDamageSound.class, String.class, "big fall damage sound", "livingentities");
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    @Nullable
    public String convert(LivingEntity e) {
        return e.getFallDamageSoundBig().name();

    }

    @Override
    protected @NotNull String getPropertyName() {
        return "big fall damage sound";
    }
}