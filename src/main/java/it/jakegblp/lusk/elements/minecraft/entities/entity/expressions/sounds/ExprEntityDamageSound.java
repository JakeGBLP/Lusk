package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions.sounds;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Name("Entity - Damage/Hurt Sound")
@Description("Returns the Sound this entity will make on damage.")
@Examples({"broadcast damage sound of target"})
@Since("1.0.4")
@SuppressWarnings("unused")
public class ExprEntityDamageSound extends SimplePropertyExpression<LivingEntity, Sound> {
    static {
        register(ExprEntityDamageSound.class, Sound.class, "(damage|hurt) sound", "livingentities");
    }

    @Override
    public @NotNull Class<? extends Sound> getReturnType() {
        return Sound.class;
    }

    @Override
    @Nullable
    public Sound convert(LivingEntity e) {
        return e.getHurtSound();
    }

    @Override
    protected @NotNull String getPropertyName() {
        return "damage sound";
    }
}