package it.jakegblp.lusk.skript.modules.base.expressions;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.bukkitutil.EntityUtils;
import ch.njol.skript.entity.EntityData;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class ExprNamespacedKeyOf extends SimplePropertyExpression<Object, NamespacedKey> {

    public static void register(SyntaxRegistry syntaxRegistry) {
        AddonUtils.registerPropertyExpression(syntaxRegistry, ExprNamespacedKeyOf.class, ExprNamespacedKeyOf::new, NamespacedKey.class,
                "namespaced[ ]key", "keyeds/blocks/blockdatas/entities/entitydatas/itemtypes");
    }

    @Override
    public @Nullable NamespacedKey convert(Object from) {
        return switch (from) {
            case Keyed keyed -> keyed.getKey();
            case BlockData blockData -> blockData.getMaterial().getKey();
            case Entity entity -> entity.getType().getKey();
            case ItemType itemType -> itemType.getMaterial().getKey();
            case EntityData<?> entityData -> {
                EntityType entityType = EntityUtils.toBukkitEntityType(entityData);
                yield entityType != null ? entityType.getKey() : null;
            }
            default -> null;
        };
    }

    @Override
    protected String getPropertyName() {
        return "namespaced key";
    }

    @Override
    public Class<? extends NamespacedKey> getReturnType() {
        return NamespacedKey.class;
    }
}
