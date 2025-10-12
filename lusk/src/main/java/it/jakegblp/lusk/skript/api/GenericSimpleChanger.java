package it.jakegblp.lusk.skript.api;

import ch.njol.skript.classes.Changer;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.Arrays;

public interface GenericSimpleChanger<C, V> extends Changer<C> {

    @NotNull Class<@NotNull V> getValueClass();

    @NotNull Class<@NotNull V[]> getValueArrayClass();

    @NotNull ChangeMode[] getAllowedChangeModes();

    boolean isSingle();

    @Override
    default Class<?> @Nullable [] acceptChange(ChangeMode mode) {
        return ArrayUtils.contains(getAllowedChangeModes(), mode) ? (
                switch (mode) {
                    case RESET, DELETE -> new Class[0];
                    default -> new Class[]{isSingle() ? getValueClass() : getValueArrayClass()};
                }) : null;
    }

    @Override
    default void change(C[] what, Object @Nullable [] delta, ChangeMode mode) {
        boolean single;
        V changeValue;
        V[] changeValues;
        SimpleChanger<C, V> simpleChanger;
        SimplePluralChanger<C, V> simplePluralChanger;
        switch (mode) {
            case DELETE -> {
                for (C c : what) {
                    delete(c);
                }
                return;
            }
            case RESET -> {
                for (C c : what) {
                    reset(c);
                }
                return;
            }
            default -> {
                assert delta != null;
                single = isSingle();
                if (single) {
                    simpleChanger = (SimpleChanger<C, V>) this;
                    simplePluralChanger = null;
                    changeValue = (V) delta[0];
                    changeValues = null;
                } else {
                    simpleChanger = null;
                    simplePluralChanger = (SimplePluralChanger<C, V>) this;
                    changeValue = null;
                    changeValues = Arrays.stream(delta)
                            .filter(o -> getValueClass().isInstance(o))
                            .map(o -> getValueClass().cast(o))
                            .toArray(size -> (V[]) Array.newInstance(getValueClass(), size));;
                }
            }
        }
        switch (mode) {
            case SET -> {
                for (C c : what) {
                    if (single)
                        simpleChanger.set(c, changeValue);
                    else
                        simplePluralChanger.set(c, changeValues);
                }
            }
            case ADD -> {
                for (C c : what) {
                    if (single)
                        simpleChanger.add(c, changeValue);
                    else
                        simplePluralChanger.add(c, changeValues);
                }
            }
            case REMOVE -> {
                for (C c : what) {
                    if (single)
                        simpleChanger.remove(c, changeValue);
                    else
                        simplePluralChanger.remove(c, changeValues);
                }
            }
            case REMOVE_ALL -> {
                for (C c : what) {
                    if (single)
                        simpleChanger.removeAll(c, changeValue);
                    else
                        simplePluralChanger.removeAll(c, changeValues);
                }
            }
        }
    }

    default void delete(C change) {}
    default void reset(C change) {}
}
