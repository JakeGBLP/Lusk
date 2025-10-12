package it.jakegblp.lusk.nms.core.world.entity;

import it.jakegblp.lusk.common.ReflectionUtils;
import it.jakegblp.lusk.nms.core.util.NMSObject;

import org.bukkit.entity.Entity;

public abstract class FlagByte<B extends BitFlag<T>, E extends Entity, T> implements NMSObject<Byte>, Cloneable {

    public static <B extends Enum<B> & BitFlag<?>, E extends Entity> FlagByte<B, E, ?> dynamic(Class<? extends FlagByte<B, E, ?>> byteFlagClass) {
        return (FlagByte<B, E, ?>) ReflectionUtils.newInstance(ReflectionUtils.getDeclaredConstructor(byteFlagClass));
    }

    protected byte flags;

    public FlagByte() {
    }

    public FlagByte(byte flags) {
        this.flags = flags;
    }

    public abstract Class<B> getBitFlagClass();

    public abstract Class<E> getFlagHolderClass();

    public B[] getConstants() {
        return getBitFlagClass().getEnumConstants();
    }

    public T get(B component) {
        int bits = (flags & component.getMask()) >> component.getShift();
        return component.decode(bits);
    }

    public void setUnsafe(B component, Object newValue) {
        if (!component.getValueClass().isInstance(newValue))
            throw new IllegalArgumentException("Passed incorrect class: "+newValue.getClass().getName()+", expected: "+component.getValueClass().getName());
        set(component, (T) newValue);
    }

    public void set(B component, T newValue) {
        int encoded = component.encode(newValue) << component.getShift();
        flags = (byte) ((flags & ~component.getMask()) | (encoded & component.getMask()));
    }

    @Override
    public Byte asNMS() {
        return flags;
    }

    @Override
    public abstract FlagByte<B, E, T> clone();
}
