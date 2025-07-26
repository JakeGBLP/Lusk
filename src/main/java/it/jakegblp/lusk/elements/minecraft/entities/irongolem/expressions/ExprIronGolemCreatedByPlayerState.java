package it.jakegblp.lusk.elements.minecraft.entities.irongolem.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

@Name("Iron Golem - was Created By a Player (Property)")
@Description("Returns whether or not an iron golem was created by a player.\nCan be set and reset.")
@Examples({"broadcast iron golem created by a player property of {_target}"})
@Since("1.0.3")
@SuppressWarnings("unused")
public class ExprIronGolemCreatedByPlayerState extends SimpleBooleanPropertyExpression<LivingEntity> {
    static {
        register(ExprIronGolemCreatedByPlayerState.class, Boolean.class, "iron golem", "[was] created by ([a] player|players)", "livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return from instanceof IronGolem ironGolem && ironGolem.isPlayerCreated();
    }

    @Override
    protected String getPropertyName() {
        return "iron golem was created by a player state";
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof IronGolem ironGolem) {
            ironGolem.setPlayerCreated(to);
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
}