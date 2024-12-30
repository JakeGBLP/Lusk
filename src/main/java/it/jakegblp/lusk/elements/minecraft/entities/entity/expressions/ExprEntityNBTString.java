package it.jakegblp.lusk.elements.minecraft.entities.entity.expressions;

import ch.njol.skript.doc.*;
import it.jakegblp.lusk.api.skript.SimplerPropertyExpression;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntitySnapshot;
import org.jetbrains.annotations.Nullable;

@Name("Entity/Snapshot - NBT String")
@Description("Gets the provided entities or entity snapshots as their NBT string.\n" +
        "Don't rely on this as the structure of the results can change across versions.")
@Examples("set {_nbtString} to entity string nbt of {_entity}")
@Since("1.3")
@RequiredPlugins("1.20.5+")
@SuppressWarnings("UnstableApiUsage")
public class ExprEntityNBTString extends SimplerPropertyExpression<Object, String> {

    static {
        register(ExprEntityNBTString.class, String.class, "entity (nbt string|string nbt)", "entities/entitysnapshots");
    }

    @Override
    public @Nullable String convert(Object from) {
        if (from instanceof Entity entity) return entity.getAsString();
        if (from instanceof EntitySnapshot snapshot) return snapshot.getAsString();
        return null;
    }

    @Override
    protected String getPropertyName() {
        return "entity nbt string";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
