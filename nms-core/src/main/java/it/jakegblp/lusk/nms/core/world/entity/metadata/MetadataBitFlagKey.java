package it.jakegblp.lusk.nms.core.world.entity.metadata;

import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.nms.core.AbstractNMS.NMS;

@Getter
@ToString
public class MetadataBitFlagKey<
        E extends Entity,
        B extends BitFlag<T>,
        F extends FlagByte<B, E,?>,
        T
        > extends MetadataKey<E, T> {

    private final MetadataKey<E, F> parentKey;
    private final B bitFlag;

    public MetadataBitFlagKey(MetadataKey<E, F> parentKey, B bitFlag, AbstractNMS nms) {
        super(parentKey.id(), parentKey.entityClass(), (Class<T>) Boolean.class, nms); // todo: check if this is actually needed, same thing below
        this.parentKey = parentKey;
        this.bitFlag = bitFlag;
    }

    public MetadataBitFlagKey(MetadataKey<E, F> parentKey, B bitFlag) {
        this(parentKey, bitFlag, NMS);
    }

    @Override
    public @NotNull Class<T> frontEndClass() {
        return (Class<T>) bitFlag.getValueClass();
    }

    @Override
    public boolean canBeSetTo(@NotNull Object object) {
        return object instanceof Boolean;
    }
}