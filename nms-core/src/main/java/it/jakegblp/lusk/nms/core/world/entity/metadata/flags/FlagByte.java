package it.jakegblp.lusk.nms.core.world.entity.metadata.flags;

import it.jakegblp.lusk.common.reflection.SimpleClass;
import it.jakegblp.lusk.nms.core.util.NMSObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Entity;

@NoArgsConstructor
@AllArgsConstructor
public abstract class FlagByte<B extends BitFlag<T>, E extends Entity, T> implements NMSObject<Byte> {

    public static <B extends Enum<B> & BitFlag<?>, E extends Entity> FlagByte<B, E, ?> dynamic(Class<? extends FlagByte<B, E, ?>> byteFlagClass) {
        return SimpleClass.quickInstance(byteFlagClass);
    }

    protected byte flags;

    @Setter
    @Getter
    public byte raw;

    public FlagByte(byte flags){
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

    @SuppressWarnings("unchecked")
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
}
