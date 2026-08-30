package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.Function;

@NullMarked
public abstract class ComponentEntityDataKey<BaseEntity extends Entity, ParentType, ParentKey extends EntityDataKey<BaseEntity, ParentType, ?, ?, ?>, Type> extends EntityDataKey<BaseEntity, Type, Type, Type, Type> {
    @Getter
    protected final ParentKey parentKey;

    protected ComponentEntityDataKey(ParentKey parentKey, String name, Class<Type> prettyType, Function<BaseEntity, Type> getter, @Nullable BiConsumer<BaseEntity, Type> setter, Type defaultValue) {
        super(name, parentKey.id, parentKey.entityClasses, parentKey.serializer, prettyType, getter, setter, defaultValue);
        this.parentKey = parentKey;
    }

    public abstract ParentType createParentObject(Type value);

}