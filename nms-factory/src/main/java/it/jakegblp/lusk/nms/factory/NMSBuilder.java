package it.jakegblp.lusk.nms.factory;

import it.jakegblp.lusk.common.reflection.SimpleClass;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.adapters.*;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

@Setter
public class NMSBuilder {

    private final int major, minor, patch;
    private JavaPlugin plugin;
    private @SuppressWarnings("rawtypes") SharedBehaviorAdapter sharedBehaviorAdapter;
    private SharedBiomeAdapter sharedBiomeAdapter;
    private PlayerRotationPacketAdapter playerRotationPacketAdapter;
    private PlayerPositionPacketAdapter<?, ?> playerPositionPacketAdapter;

    public NMSBuilder(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public AbstractNMS<?> build() {
        String version = "v" + major + "_" + minor + (patch == 0 ? "" : "_" + patch);
        return (AbstractNMS<?>) SimpleClass.of("it.jakegblp.lusk.nms.impl." + version + "." + version).getConstructor(
                JavaPlugin.class,
                SharedBehaviorAdapter.class,
                PlayerRotationPacketAdapter.class,
                PlayerPositionPacketAdapter.class,
                SharedBiomeAdapter.class).newInstance(
                plugin,
                sharedBehaviorAdapter,
                playerRotationPacketAdapter,
                playerPositionPacketAdapter,
                sharedBiomeAdapter
        );
    }

}
