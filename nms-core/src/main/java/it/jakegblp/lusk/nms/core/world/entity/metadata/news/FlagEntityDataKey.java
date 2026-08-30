package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.Flag;
import it.jakegblp.lusk.nms.core.world.entity.metadata.flags.FlagByte;
import lombok.Getter;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

@Getter
@NullMarked
public class FlagEntityDataKey<BaseEntity extends Entity, FlagsType extends FlagByte<BaseEntity>, Type> extends ComponentEntityDataKey<BaseEntity, FlagsType, FlagsEntityDataKey<BaseEntity, FlagsType>, Type> {
    protected final Flag<BaseEntity, Type, ?> flag;

    public FlagEntityDataKey(FlagsEntityDataKey<BaseEntity, FlagsType> parentKey, Flag<BaseEntity, Type, ?> flag) {
        super(parentKey, flag.name(), flag.getValueClass(), flag::get, flag::set, flag.getDefaultValue());
        this.flag = flag;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public FlagsType createParentObject(Type value) {
        FlagByte parentType = parentKey.getDefaultValue();
        parentType.set(flag, value);
        return (FlagsType) parentType;
    }

    @Override
    public String toString() {
        return "FlagEntityDataKey{parentKey=" + parentKey + ", flag=" + flag + "}";
    }
}
