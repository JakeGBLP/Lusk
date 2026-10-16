package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.Keyed;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@NullMarked
public class RegistryIntEntityDataKey<BaseEntity extends Entity, Type extends Keyed> extends RegistryEntityDataKey<BaseEntity, Type, Object> {

    @SuppressWarnings("unchecked")
    public RegistryIntEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<Type> prettyType, Function<BaseEntity, Type> getter, BiConsumer<BaseEntity, Type> setter, io.papermc.paper.registry.RegistryKey<Type> registryKey, Type defaultValue) {
        super(name, id, entityClasses, serializer, prettyType, Object.class, getter, setter, integer -> (Type) NMS.fromNMS(integer), type -> NMS.toNMS(type), registryKey, defaultValue);
    }

    @Override
    public String toString() {
        return "RegistryIntEntityDataKey{type=" + prettyType + ", registryKey=" + registryKey + "}";
    }
}
