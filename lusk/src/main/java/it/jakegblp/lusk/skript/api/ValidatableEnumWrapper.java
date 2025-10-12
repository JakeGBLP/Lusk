package it.jakegblp.lusk.skript.api;

import it.jakegblp.lusk.common.Validatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValidatableEnumWrapper<E extends Enum<E> & Validatable<?>> extends EnumWrapper<E> {

    public ValidatableEnumWrapper(@NotNull Class<E> c, @Nullable String prefix, @Nullable String suffix) {
        super(c, Validatable.filterValidatableEnum(c), prefix, suffix);
    }

    public ValidatableEnumWrapper(@NotNull Class<E> c) {
        this(c, null, null);
    }
}