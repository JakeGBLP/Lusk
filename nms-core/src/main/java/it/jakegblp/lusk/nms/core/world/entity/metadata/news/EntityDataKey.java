package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import com.google.common.base.Preconditions;
import it.jakegblp.lusk.common.BiDirectionalFunction;
import it.jakegblp.lusk.common.Immutable;
import it.jakegblp.lusk.nms.core.util.NullabilityUtils;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataEntry;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

@NullMarked
public abstract class EntityDataKey<BaseEntity extends Entity, PrettyType, InternalType, PrettyTypeGeneric, InternalTypeGeneric> implements Immutable<EntityDataKey<BaseEntity, PrettyType, InternalType, PrettyTypeGeneric, InternalTypeGeneric>> {

    @Getter
    protected final String name;
    @Getter
    protected final @Range(from = 0, to = 255) int id;
    @Getter
    protected final EntityClasses<BaseEntity> entityClasses;
    @Getter
    protected final EntityDataSerializer<?, ?> serializer;
    @Getter
    protected final Class<PrettyTypeGeneric> prettyType;
    @Getter
    protected final @Nullable Class<InternalTypeGeneric> internalType;
    protected final @Nullable BiDirectionalFunction<InternalType, PrettyType> typeConverter;
    protected final Function<BaseEntity, InternalType> getter;
    protected final @Nullable BiConsumer<BaseEntity, InternalType> setter;

    protected final PrettyType defaultValue;

    protected EntityDataKey(
            String name,
            @Range(from = 0, to = 255) int id,
            EntityClasses<BaseEntity> entityClasses,
            EntityDataSerializer<?, ?> serializer,
            Class<PrettyTypeGeneric> prettyType,
            @Nullable Class<InternalTypeGeneric> internalType,
            Function<BaseEntity, InternalType> getter,
            @Nullable BiConsumer<BaseEntity, InternalType> setter,
            @Nullable BiDirectionalFunction<InternalType, PrettyType> typeConverter,
            PrettyType defaultValue
    ) {
        this.name = name;
        this.id = id;
        this.entityClasses = entityClasses;
        this.serializer = serializer;
        this.prettyType = prettyType;
        this.internalType = internalType;
        this.getter = getter;
        this.setter = setter;
        this.typeConverter = typeConverter;
        this.defaultValue = defaultValue;
    }

    protected EntityDataKey(
            String name,
            @Range(from = 0, to = 255) int id,
            EntityClasses<BaseEntity> entityClasses,
            EntityDataSerializer<?, ?> serializer,
            Class<PrettyTypeGeneric> prettyType,
            Class<InternalTypeGeneric> internalType,
            Function<BaseEntity, InternalType> getter,
            @Nullable BiConsumer<BaseEntity, InternalType> setter,
            Function<InternalType, PrettyType> prettifier,
            Function<PrettyType, InternalType> internalizer,
            PrettyType defaultValue
    ) {
        this(name, id, entityClasses, serializer, prettyType, internalType, getter, setter, BiDirectionalFunction.of(prettifier, internalizer), defaultValue);
    }

    protected EntityDataKey(
            String name,
            @Range(from = 0, to = 255) int id,
            EntityClasses<BaseEntity> entityClasses,
            EntityDataSerializer<?, ?> serializer,
            Class<PrettyTypeGeneric> prettyType,
            Function<BaseEntity, InternalType> getter,
            @Nullable BiConsumer<BaseEntity, InternalType> setter,
            PrettyType defaultValue
    ) {
        this(name, id, entityClasses, serializer, prettyType, null, getter, setter, null, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public PrettyType asPretty(InternalType internal) {
        return typeConverter == null ? (PrettyType) internal : typeConverter.asSecond(internal);
    }

    @SuppressWarnings("unchecked")
    public InternalType asInternal(PrettyType pretty) {
        return typeConverter == null ? (InternalType) pretty : typeConverter.asFirst(pretty);//((internalType != null && internalType.isInstance(pretty)) ? (InternalType) pretty : typeConverter.asFirst(pretty));
    }

    public InternalType getInternalValue(BaseEntity entity) {
        return getter.apply(entity);
    }

    public void setInternalValue(BaseEntity entity, InternalType value) {
        Preconditions.checkNotNull(value, "This metadata key is read only. Use Key#isReadOnly()");
        assert setter != null;
        setter.accept(entity, value);
    }

    public InternalType getDefaultInternalValue() {
        return asInternal(defaultValue);
    }

    public PrettyType get(BaseEntity entity) {
        return asPretty(getInternalValue(entity));
    }

    public boolean isReadOnly() {
        return setter == null;
    }

    public void set(BaseEntity entity, PrettyType value) {
        Preconditions.checkNotNull(value, "This metadata key is read only. Use Key#isReadOnly()");
        assert setter != null;
        setInternalValue(entity, asInternal(value));
    }

    public boolean canBeSetTo(Object object) {
        if (this instanceof OptionalEntityDataKey) {
            var optional = (Optional<?>) object;
            return optional.map(prettyType::isInstance).orElse(true);
        } else return prettyType.isInstance(object);
    }

    @Contract("-> new")
    public PrettyType getDefaultValue() {
        return NullabilityUtils.copyIfNotNull(defaultValue);
    }

    public MetadataEntry<BaseEntity, PrettyType> asItem(PrettyType value) {
        return new MetadataEntry<>(this, value);
    }
}
