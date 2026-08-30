package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import it.jakegblp.lusk.nms.core.world.entity.serialization.EntityDataSerializer;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class FlagsEntityDataKey<BaseEntity extends Entity, FlagsType extends FlagByte<BaseEntity>> extends EntityDataKey<BaseEntity, FlagsType, Byte, FlagsType, Byte> {

    public FlagsEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<FlagsType> prettyType, FlagsType defaultValue) {
        super(name, id, entityClasses, serializer, prettyType, Byte.class, entity -> FlagByte.dynamic(prettyType, entity).asNMS(), (entity, aByte) -> FlagByte.dynamic(prettyType, aByte).apply(entity), aByte -> FlagByte.dynamic(prettyType, aByte), FlagByte::asNMS, defaultValue);
    }

    public FlagsEntityDataKey(String name, @Range(from = 0, to = 255) int id, EntityClasses<BaseEntity> entityClasses, EntityDataSerializer<?, ?> serializer, Class<FlagsType> prettyType) {
        super(name, id, entityClasses, serializer, prettyType, Byte.class, entity -> FlagByte.dynamic(prettyType, entity).asNMS(), (entity, aByte) -> FlagByte.dynamic(prettyType, aByte).apply(entity), aByte -> FlagByte.dynamic(prettyType, aByte), FlagByte::asNMS, FlagByte.dynamic(prettyType));
    }

    public <SubType> FlagEntityDataKey<BaseEntity, FlagsType, SubType> flag(it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag<BaseEntity, SubType, ?> flag) {
        return new FlagEntityDataKey<>(this, flag);
    }

    @Override
    public String toString() {
        return "FlagsEntityDataKey{type=" + prettyType + "}";
    }

}
