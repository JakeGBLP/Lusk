package it.jakegblp.lusk.common;

import java.util.function.Predicate;

public interface VersionedBuilder<T extends VersionedBuilder<T, S>, S> {
    T withIf(Predicate<Version> condition, S value);
    T withOrElse(Predicate<Version> condition, S value, S fallback);
}
