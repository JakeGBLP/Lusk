package it.jakegblp.lusk.nms.core.world.entity.metadata.flags;

import it.jakegblp.lusk.common.reflection.SimpleClass;
import it.jakegblp.lusk.nms.core.util.NMSObject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
public abstract class FlagByte<E extends Entity> implements NMSObject<Byte> {

    public static <E extends Entity, F extends FlagByte<E>> F dynamic(Class<F> byteFlagClass) {
        return SimpleClass.quickInstance(byteFlagClass);
    }

    public static <E extends Entity, F extends FlagByte<E>> F dynamic(Class<F> byteFlagClass, byte flags) {
        return SimpleClass.quickInstance(byteFlagClass, new Class[] {byte.class}, flags);
    }

    public static <E extends Entity, F extends FlagByte<E>> F dynamic(Class<F> byteFlagClass, E entity) {
        return SimpleClass.quickInstance(byteFlagClass, entity);
    }

    protected byte flags;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public FlagByte(E entity) {
        for (Flag constant : getConstants())
            if (constant.isUsed())
                set(constant, constant.get(entity));
    }

    public abstract Class<E> getFlagHolderClass();

    /**
     *
     * @return whether the generic entity type of this class is the exact type represented by this flag byte's
     *  <br>implementation; if false, {@link #getFlagHolderClasses()} will contain two or more classes that extend
     *  {@link E}
     */
    public final boolean isOneToOne() {
        return getFlagHolderClasses().size() == 1;
    }

    public List<Class<? extends E>> getFlagHolderClasses() {
        return List.of(getFlagHolderClass());
    }

    public abstract Flag<E, ?, ?>[] getConstants();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void apply(E entity) {
        for (Flag constant : getConstants())
            if (constant.isUsed())
                constant.set(entity, get(constant));
    }

    public <T, F extends Flag<E, T, F>> T get(@NotNull F component) {
        int bits = (flags & component.getMask()) >> component.getShift();
        return component.decode(bits);
    }

    @SuppressWarnings("unchecked")
    public <T, F extends Flag<E, T, F>> void setUnsafe(@NotNull F component, Object newValue) {
        if (!component.getValueClass().isInstance(newValue))
            throw new IllegalArgumentException("Passed incorrect type: "+newValue.getClass().getName()+", expected: "+component.getValueClass().getName());
        set(component, (T) newValue);
    }

    public <T, F extends Flag<E, T, F>> void set(@NotNull F component, T newValue) {
        int encoded = component.encode(newValue) << component.getShift();
        flags = (byte) ((flags & ~component.getMask()) | (encoded & component.getMask()));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String toString() {
        return "FlagByte{value=[" + Arrays.stream(getConstants()).filter(Flag::isUsed).map(flag -> this.get((Flag)flag).toString()).collect(Collectors.joining(", ")) + "]}";
    }

    @Override
    public Byte asNMS() {
        return flags;
    }
}
