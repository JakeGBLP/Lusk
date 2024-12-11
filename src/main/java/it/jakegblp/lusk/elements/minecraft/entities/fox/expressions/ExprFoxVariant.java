package it.jakegblp.lusk.elements.minecraft.entities.fox.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.Fox;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.LuskUtils.registerVerbosePropertyExpression;

@Name("Fox - Variant")
@Description("Gets the current type of this fox.\nCan bet set.")
@Examples("set fox type of {_fox} to red_fox_type")
@Since("1.3")
public class ExprFoxVariant extends SimplerPropertyExpression<Fox,Fox.Type> {

    static {
        registerVerbosePropertyExpression(ExprFoxVariant.class, Fox.Type.class,"fox (variant|type)","foxes");
    }

    @Override
    public boolean allowSet() {
        return true;
    }

    @Override
    public void set(Fox from, Fox.Type to) {
        from.setFoxType(to);
    }

    @Override
    public @Nullable Fox.Type convert(Fox from) {
        return from.getFoxType();
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
