package it.jakegblp.lusk.nms.core.world.entity.metadata.news;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NullMarked;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@NullMarked
public record EntityClasses<BaseEntity extends Entity>(List<Class<? extends BaseEntity>> entities) implements Iterable<Class<? extends BaseEntity>> {

    public EntityClasses(Class<? extends BaseEntity> entity) {
        this(List.of(entity));
    }

    public EntityClasses {
        Preconditions.checkArgument(!entities.isEmpty(), "missing entities");
        entities = Collections.unmodifiableList(entities);
    }

    public boolean isSingle() {
        return entities.size() == 1;
    }

    public Class<? extends BaseEntity> getSingle() {
        return entities.getFirst();
    }

    public boolean isAssignableFrom(Iterable<Class<? extends Entity>> other) {
        for (Class<? extends Entity> entityClass : other)
            if (isAssignableFrom(entityClass))
                return true;
        return false;
    }

    public boolean isAssignableFrom(Class<? extends Entity> unknownEntityClass) {
        for (Class<? extends Entity> entityClass : entities)
            if (entityClass.isAssignableFrom(unknownEntityClass))
                return true;
        return false;
    }

    @Override
    public Iterator<Class<? extends BaseEntity>> iterator() {
        return entities.iterator();
    }
}
