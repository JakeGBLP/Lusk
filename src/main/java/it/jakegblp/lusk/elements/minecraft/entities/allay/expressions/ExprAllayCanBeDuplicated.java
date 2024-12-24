package it.jakegblp.lusk.elements.minecraft.entities.allay.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimpleBooleanPropertyExpression;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

@Name("Allay - can Be Duplication (Property)")
@Description("Returns whether or not the allay can duplicate itself.\nCan be set.")
@Examples({"broadcast duplication state of target"})
@Since("1.0.2, 1.3 (Plural)")
@DocumentationId("9054")
@SuppressWarnings("unused")
public class ExprAllayCanBeDuplicated extends SimpleBooleanPropertyExpression<Entity> {

    static {
        register(ExprAllayCanBeDuplicated.class,Boolean.class,"[allay]", "(can be duplicated|duplication)", "livingentities");
    }

    @Override
    public @Nullable Boolean convert(Entity from) {
        return from instanceof Allay allay && allay.canDuplicate();
    }

    @Override
    public void set(Entity from, Boolean to) {
        if (from instanceof Allay allay) {
            allay.setCanDuplicate(to);
        }
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    protected String getPropertyName() {
        return "can be duplicated";
    }
}