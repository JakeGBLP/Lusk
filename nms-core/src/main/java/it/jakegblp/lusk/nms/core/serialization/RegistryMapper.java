package it.jakegblp.lusk.nms.core.serialization;

import org.bukkit.Keyed;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RegistryMapper<F, T extends Keyed> extends Mapper<F, T> {

    @Override
    default RegistryMapper<F, T> withVariant(int variant) {
        throw new UnsupportedOperationException("Must be an RegistryMapperImpl");
    }
}
