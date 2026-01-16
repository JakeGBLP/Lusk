package it.jakegblp.lusk.nms.core;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.adapters.SharedBehaviorAdapter;
import it.jakegblp.lusk.nms.core.adapters.SharedBiomeAdapter;
import it.jakegblp.lusk.nms.core.injection.InjectionListener;
import it.jakegblp.lusk.nms.core.protocol.packets.Packet;
import it.jakegblp.lusk.nms.core.util.NMSObject;
import it.jakegblp.lusk.nms.core.util.SimpleByteBuf;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static it.jakegblp.lusk.common.reflection.SimpleClass.classOf;

public abstract class AbstractNMS {

    public static AbstractNMS NMS;
    @Delegate
    @SuppressWarnings("rawtypes")
    protected final SharedBehaviorAdapter sharedBehaviorAdapter;
    @Getter
    protected final JavaPlugin plugin;
    @Getter
    protected final Version version;
    @Delegate
    protected final SharedBiomeAdapter sharedBiomeAdapter;

    public AbstractNMS(
            JavaPlugin plugin,
            Version version,
            @SuppressWarnings("rawtypes")
            SharedBehaviorAdapter sharedBehaviorAdapter,
            SharedBiomeAdapter sharedBiomeAdapter
    ) {
        this.plugin = plugin;
        this.version = version;
        this.sharedBehaviorAdapter = sharedBehaviorAdapter;
        this.sharedBiomeAdapter = sharedBiomeAdapter;
        Bukkit.getPluginManager().registerEvents(new InjectionListener(), plugin);
        init();
    }

    public abstract void init();

    public @Nullable Object toNMSObject(@Nullable Object object) {
        if (object == null)
            throw new RuntimeException("Object null for toNMSObject");
        else if (object instanceof NMSObject<?> nmsObject) return nmsObject.asNMS();
        else if (isCodecFromClass(object.getClass())) return object;
        else return toNMS(object);
    }

    /**
     * This method takes an NMS Packet and returns a non NMS Packet
     *
     * @param object an NMS Packet
     * @return the packet in a common form
     */
    public @NotNull Packet fromNMSPacket(@NotNull Object object) {
        if (isNMSClientBundlePacket(object)) return fromNMSClientBundlePacket(object);
        throw new IllegalArgumentException("Could not convert nms packet " + object.getClass().getName());
    }

    public @NotNull Object fromNMSObject(@NotNull Object object) {
        if (isNMSPacket(object)) return fromNMSPacket(object);
        throw new IllegalArgumentException("Could not convert nms object " + object.getClass().getName());

    }

    public <F> void writeNMS(@NotNull F from, @NotNull SimpleByteBuf buffer) {
        getFirstNMSCodec(classOf(from)).writeFrom(from, buffer);
    }

    public <F> F readNMS(@NotNull Class<F> fromClass, @NotNull SimpleByteBuf buffer) {
        return fromClass.cast(getFirstNMSCodec(fromClass).readFrom(buffer));
    }

    public <T> void write(@NotNull T to, @NotNull SimpleByteBuf buffer) {
        getFirstCodec(classOf(to)).writeTo(to, buffer);
    }

    public <T> T read(@NotNull Class<T> toClass, @NotNull SimpleByteBuf buffer) {
        return toClass.cast(getFirstCodec(toClass).readTo(buffer));
    }
}