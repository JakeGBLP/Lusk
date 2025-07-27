package it.jakegblp.lusk.elements.minecraft.entities.armadillo.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.Armadillo;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_21_5;

@Name("Armadillo - State")
@Description("Gets the State of one or more Armadillos, cannot be set.")
@Examples("set {_state} to armadillo state of {_armadillo}")
@Since("1.3.8")
@RequiredPlugins("1.21.5+")
public class ExprArmadilloState extends SimplerPropertyExpression<LivingEntity, Armadillo.State> {

    static {
        if (MINECRAFT_1_21_5)
            register(ExprArmadilloState.class, Armadillo.State.class, "armadillo state", "livingentities");
    }

    @Override
    public @Nullable Armadillo.State convert(LivingEntity from) {
        return from instanceof Armadillo armadillo ? armadillo.getState() : null;
    }

    @Override
    protected String getPropertyName() {
        return "armadillo state";
    }

    @Override
    public Class<? extends Armadillo.State> getReturnType() {
        return Armadillo.State.class;
    }
}
