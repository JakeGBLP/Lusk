package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.EntityUtils.isSitting;
import static it.jakegblp.lusk.utils.EntityUtils.setIsSitting;

@Name("Entity - is Sitting (Property)")
@Description("""
        Returns whether an entity is sat. Can be set.
        This works for Cats, Wolves, Parrots, Pandas and Foxes.
        On Paper 1.20.1+ it applies for some other entities which have a SITTING pose.
        """)
@Examples({"broadcast sitting state of target"})
@Since("1.0.2")
@DocumentationId("9057")
@SuppressWarnings("unused")
public class ExprEntityIsSitting extends SimpleBooleanPropertyExpression<Entity> {

    static {
        register(ExprEntityIsSitting.class,Boolean.class,"[is] s(at|it[ting]) [down]", "entities");
    }

    @Override
    public @Nullable Boolean convert(Entity from) {
        return isSitting(from);
    }

    @Override
    public void set(Entity from, Boolean to) {
        setIsSitting(from,to);
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "is sitting down";
    }
}