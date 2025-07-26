package it.jakegblp.lusk.elements.minecraft.entities.ocelot.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.jetbrains.annotations.Nullable;

@Name("Ocelot - is Trusting (Property)")
@Description("Returns whether or not an ocelot trusts players.\nCan be set.")
@Examples({"broadcast ocelot trusting state of target"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprOcelotTrustingState extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        register(ExprOcelotTrustingState.class, Boolean.class, "ocelot", "([is] trusting|trust[s])", "livingentities");
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        if (from instanceof Ocelot ocelot) {
            ocelot.setTrusting(to);
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
        return from instanceof Ocelot ocelot && ocelot.isTrusting();
    }

    @Override
    protected String getPropertyName() {
        return "ocelot is trusting state";
    }
}