package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.EntityUtils.isScreaming;
import static it.jakegblp.lusk.utils.EntityUtils.setIsScreaming;

@Name("Entity - is Screaming (Property)")
@Description("Returns the Screaming Property of an entity.\nThis is for Endermen (Requires Paper and 1.18.2+) and Goats.")
@Examples({"broadcast screaming state of target"})
@Since("1.0.2")
@SuppressWarnings("unused")
public class ExprEntityIsScreaming extends SimpleBooleanPropertyExpression<LivingEntity> {

    static {
        register(ExprEntityIsScreaming.class, Boolean.class, "[is] screaming", "livingentities");
    }

    @Override
    public @Nullable Boolean convert(LivingEntity from) {
        return isScreaming(from);
    }

    @Override
    public void set(LivingEntity from, Boolean to) {
        setIsScreaming(from,to);
    }

    @Override
    public void reset(LivingEntity from) {
        setIsScreaming(from, false);
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
    protected String getPropertyName() {
        return "is screaming";
    }
}