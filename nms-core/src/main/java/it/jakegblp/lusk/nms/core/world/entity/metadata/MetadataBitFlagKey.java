package it.jakegblp.lusk.nms.core.world.entity.metadata;

import it.jakegblp.lusk.nms.core.world.entity.BitFlag;
import it.jakegblp.lusk.nms.core.world.entity.FlagByte;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntitySerializerKey;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Entity;

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

    public MetadataBitFlagKey(MetadataKey<E, F> parentKey, B bitFlag) {
        super(parentKey.id(), parentKey.entityClass(), null);
        this.parentKey = parentKey;
        this.bitFlag = bitFlag;
    }

    @Override
    public Class<T> valueClass() {
        return (Class<T>) bitFlag.getValueClass();
    }

    @Override
    public boolean canBeSetTo(Object object) {
        return object instanceof Boolean;
    }
}