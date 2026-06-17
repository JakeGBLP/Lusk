package it.jakegblp.lusk.nms.core.world.entity.attribute;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import it.jakegblp.lusk.common.Copyable;
import it.jakegblp.lusk.common.SimpleList;
import it.jakegblp.lusk.common.reflection.SimpleClass;
import it.jakegblp.lusk.nms.core.serialization.BufferSerializable;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.util.PureNMSObject;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleMaps;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AttributeSnapshot implements BufferSerializable<AttributeSnapshot>, Copyable<AttributeSnapshot>, PureNMSObject<Object>, SimpleList<AttributeModifier> {

    public static final Object2DoubleMap<Attribute> DEFAULT_ATTRIBUTE_VALUES;

    static {
        var map = new Object2DoubleOpenHashMap<Attribute>();
        var converter = SimpleClass.of("org.bukkit.craftbukkit.attribute.CraftAttribute").getMethod("bukkitToMinecraftHolder", true, true, Attribute.class);
        var holderGetter = SimpleClass.of(converter.getReturnType()).getMethod("value", false, true);
        var getter = SimpleClass.of("net.minecraft.world.entity.ai.attributes.Attribute").getMethod("getDefaultValue", false, true);
        for (Attribute attribute : RegistryAccess.registryAccess().getRegistry(RegistryKey.ATTRIBUTE))
            map.put(attribute, (double) getter.invoke(holderGetter.invoke(converter.invoke(null, attribute))));
        DEFAULT_ATTRIBUTE_VALUES = Object2DoubleMaps.unmodifiable(map);
    }

    public static double getDefaultValue(Attribute attribute) {
        return DEFAULT_ATTRIBUTE_VALUES.getDouble(attribute);
    }

    public static AttributeSnapshot of(Attributable attributable, Attribute attribute) {
        var instance = attributable.getAttribute(attribute);
        AttributeSnapshot snapshot;
        if (instance == null)
            snapshot = new AttributeSnapshot(attribute, getDefaultValue(attribute), List.of());
        else
            snapshot = new AttributeSnapshot(attribute, instance.getBaseValue(), instance.getModifiers());
        return snapshot;
    }

    protected @NotNull Attribute attribute;
    protected double base;
    @SuppressWarnings("all")
    protected @NotNull List<AttributeModifier> modifiers;

    public AttributeSnapshot(@NotNull Attribute attribute, double base, @NotNull Collection<AttributeModifier> modifiers) {
        this.attribute = attribute;
        this.base = base;
        this.modifiers = new ArrayList<>(modifiers);
    }

    public AttributeSnapshot(@NotNull Attribute attribute, double base) {
        this.attribute = attribute;
        this.base = base;
        this.modifiers = new ArrayList<>();
    }

    public AttributeSnapshot(SimpleByteBuf buffer) {
        read(buffer);
    }

    public void setModifiers(@NotNull List<AttributeModifier> modifiers) {
        this.modifiers = new ArrayList<>(modifiers);
    }

    @Override
    public void write(SimpleByteBuf buffer) {
        buffer.writeAttribute(attribute);
        buffer.writeDouble(base);
        buffer.writeCollection(modifiers, SimpleByteBuf::writeAttributeModifier);
    }

    @Override
    public void read(SimpleByteBuf buffer) {
        attribute = buffer.readAttribute();
        base = buffer.readDouble();
        modifiers = buffer.readList(SimpleByteBuf::readAttributeModifier);
    }

    @Override
    public AttributeSnapshot copy() {
        return new AttributeSnapshot(attribute, base, modifiers);
    }

    @Override
    public List<AttributeModifier> get() {
        return modifiers;
    }
}
