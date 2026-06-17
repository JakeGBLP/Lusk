package it.jakegblp.lusk.nms.core;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.adapter.SharedBehaviorAdapter;
import it.jakegblp.lusk.nms.core.injection.InjectionListener;
import it.jakegblp.lusk.nms.core.serialization.SimpleByteBuf;
import it.jakegblp.lusk.nms.core.serialization.SimpleKey;
import it.jakegblp.lusk.nms.core.serialization.TypeKey;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeyRegistry;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.util.function.Consumer;

public abstract class AbstractNMS {

    public static AbstractNMS NMS;

    @Delegate
    @SuppressWarnings("rawtypes")
    protected final SharedBehaviorAdapter sharedBehaviorAdapter;
    //@Getter
    //public final List<Class<ClientboundPacket<?>>> implementedClientboundPacketClasses;
    @Getter
    protected final JavaPlugin plugin;
    @Getter
    protected final Version version;
    @Getter
    protected final MetadataKeyRegistry metadataKeyRegistry;

    public AbstractNMS(
            JavaPlugin plugin,
            Version version,
            Consumer<AbstractNMS> abstractNMSConsumer,
            @SuppressWarnings("rawtypes") SharedBehaviorAdapter sharedBehaviorAdapter
    ) {
        this.plugin = plugin;
        this.version = version;
        this.sharedBehaviorAdapter = sharedBehaviorAdapter;
        sharedBehaviorAdapter.init(version);
        abstractNMSConsumer.accept(this);
        init();
        Bukkit.getPluginManager().registerEvents(new InjectionListener(), plugin);
        //implementedClientboundPacketClasses = CommonUtils.filterMap(bufferCodecs.getCodecs(), codec -> ClientboundPacket.class.isAssignableFrom(codec.getToClass()), simpleBufferCodec -> (Class<ClientboundPacket<?>>)simpleBufferCodec.getToClass());
        metadataKeyRegistry = new MetadataKeyRegistry(this);
    }


    public <F, T> T fromNMS(F f) {
        return getMappings().map(f);
    }

    public <F, T> F toNMS(T t) {
        return getMappings().mapBack(t);
    }

    public abstract void init();

    @NullMarked
    public <T> void write(SimpleByteBuf buffer, T to) {
        write(buffer, to.getClass(), 0);
    }

    @NullMarked
    public <T> void write(SimpleByteBuf buffer, T to, int variant) {
        write(buffer, to, new SimpleKey<>((Class<T>) to.getClass(), variant));
    }

    public <T> void write(SimpleByteBuf buffer, T to, TypeKey<T> typeKey) {
        getCodecs().encode(buffer, to, typeKey);
    }

    @NullMarked
    public <T> T read(SimpleByteBuf buffer, TypeKey<T> typeKey) {
        return getCodecs().decode(buffer, typeKey);
    }

    @NullMarked
    public <T> T read(SimpleByteBuf buffer, Class<T> type) {
        return read(buffer, type, 0);
    }

    @NullMarked
    public <T> T read(SimpleByteBuf buffer, Class<T> type, int variant) {
        return read(buffer, new SimpleKey<>(type, variant));
    }
}