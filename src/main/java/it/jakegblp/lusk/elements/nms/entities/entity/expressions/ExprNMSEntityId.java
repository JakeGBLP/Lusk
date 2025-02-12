package it.jakegblp.lusk.elements.nms.entities.entity.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import it.jakegblp.lusk.utils.EntityUtils;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_20_6;
import static it.jakegblp.lusk.utils.NMSUtils.NMS;

@Name("NMS | Entity - Protocol ID")
@Description("Returns the network protocol ID for one or more entities.\nCan be set if Lusk NMS are up-to-date.")
@Examples("broadcast entity id of target")
@Since("1.3, 1.4 (NMS)")
@DocumentationId("ExprEntityId")
public class ExprNMSEntityId extends SimplerPropertyExpression<Entity, Integer> {

    static {
        if (NMS != null || MINECRAFT_1_20_6)
            register(ExprNMSEntityId.class, Integer.class, "entity [protocol] id", "entities");
    }

    @Override
    public @Nullable Integer convert(Entity from) {
        return EntityUtils.getEntityId(from);
    }

    @Override
    public boolean allowSet() {
        return NMS != null;
    }

    @Override
    public void set(Entity from, Integer to) {
        assert NMS != null;
        NMS.setEntityId(from, to);
    }

    @Override
    protected String getPropertyName() {
        return "entity protocol id";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }
}
