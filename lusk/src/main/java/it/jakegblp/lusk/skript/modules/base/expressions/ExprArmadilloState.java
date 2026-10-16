package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.skript.api.registration.Registrations;
import org.bukkit.entity.Armadillo;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

@Name("Armadillo - State")
@Description("Gets the State of one or more Armadillos, cannot be set.")
@Example("set {_state} to armadillo state of {_armadillo}")
@Since("1.3.8")
public class ExprArmadilloState extends SimplePropertyExpression<LivingEntity, Armadillo.State> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        Registrations.propertyExpression(ExprArmadilloState.class, ExprArmadilloState::new, Armadillo.State.class)
                .propertyOf("armadillo state", "livingentities")
                .register(syntaxRegistry);
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