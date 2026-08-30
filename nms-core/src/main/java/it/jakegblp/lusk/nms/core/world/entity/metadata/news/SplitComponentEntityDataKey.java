package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiFunction;
import java.util.function.Function;

@NullMarked
public class SplitComponentEntityDataKey<BaseEntity extends Entity, ParentType, Type> extends ComponentEntityDataKey<BaseEntity, ParentType, SplitEntityDataKey<BaseEntity, ParentType>, Type> {

    protected final BiFunction<ParentType, Type, ParentType> inserter;

    public SplitComponentEntityDataKey(
            SplitEntityDataKey<BaseEntity, ParentType> parentKey,
            String name,
            Class<Type> type,
            Function<ParentType, Type> extractor,
            BiFunction<ParentType, Type, ParentType> inserter,
            Type defaultValue
    ) {
        super(parentKey, name, type, entity -> extractor.apply(parentKey.getInternalValue(entity)), (entity, value) -> parentKey.setInternalValue(entity, inserter.apply(parentKey.getInternalValue(entity), value)), defaultValue);
        this.inserter = inserter;
    }

    @Override
    public ParentType createParentObject(Type value) {
        return inserter.apply(parentKey.getDefaultValue(), value);
    }

    @Override
    public String toString() {
        return "SplitComponentEntityDataKey{parentKey=" + parentKey + "}";
    }
}