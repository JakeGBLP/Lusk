package it.jakegblp.lusk.elements.minecraft.entities.fox.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

@Name("Fox - Variant")
@Description("Gets the current type of this fox.\nCan bet set.")
@Examples("set fox type of {_fox} to red_fox_type")
@Since("1.3")
public class ExprFoxVariant extends SimplerPropertyExpression<LivingEntity,Fox.Type> {

    static {
        register(ExprFoxVariant.class, Fox.Type.class,"fox (variant|type)","livingentities");
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void set(LivingEntity from, Fox.Type to) {
        if (from instanceof Fox fox) {
            fox.setFoxType(to);
        }
    }

    @Override
    public @Nullable Fox.Type convert(LivingEntity from) {
        if (from instanceof Fox fox) {
            return fox.getFoxType();
        }
        return null;
    }

    @Override
    protected String getPropertyName() {
        return "fox variant";
    }

    @Override
    public Class<? extends Fox.Type> getReturnType() {
        return Fox.Type.class;
    }
}
