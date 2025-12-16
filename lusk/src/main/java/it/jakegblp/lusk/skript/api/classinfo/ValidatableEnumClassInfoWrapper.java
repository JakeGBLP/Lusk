package it.jakegblp.lusk.skript.api.classinfo;

import it.jakegblp.lusk.common.Validatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// todo: maybe merge into the class it extends?
public class ValidatableEnumClassInfoWrapper<E extends Enum<E> & Validatable<?>> extends EnumClassInfoWrapper<E> {

    public ValidatableEnumClassInfoWrapper(@NotNull Class<E> c, @Nullable String prefix, @Nullable String suffix) {
        super(c, Validatable.filterValidatableEnum(c), prefix, suffix);
    }

    public ValidatableEnumClassInfoWrapper(@NotNull Class<E> c) {
        this(c, null, null);
    }
}