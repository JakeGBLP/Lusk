package it.jakegblp.lusk.nms.factory;

import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.adapters.*;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import static it.jakegblp.lusk.common.ReflectionUtils.*;

@Setter
public class NMSBuilder {

    private final int major, minor, patch;
    private JavaPlugin plugin;
    private EntityTypeAdapter<?> entityTypeAdapter;
    private MajorChangesAdapter majorChangesAdapter;
    private AddEntityPacketAdapter<?> addEntityPacketAdapter;
    private EntityMetadataPacketAdapter<?> entityMetadataPacketAdapter;
    private PlayerRotationPacketAdapter playerRotationPacketAdapter;
    private ClientBundlePacketAdapter<?> clientBundlePacketAdapter;
    private SetEquipmentPacketAdapter<?> setEquipmentPacketAdapter;
    private AdventureAdapter<?, ?> adventureAdapter;
    private PlayerPositionPacketAdapter<?, ?> playerPositionPacketAdapter;

    public NMSBuilder(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public AbstractNMS<?> build() {
        String version = "v" + major + "_" + minor + (patch == 0 ? "" : "_" + patch);
        return (AbstractNMS<?>) newInstance(
                getDeclaredConstructor(
                        forClassName("it.jakegblp.lusk.nms.impl." + version + "." + version),
                        JavaPlugin.class,
                        EntityTypeAdapter.class,
                        MajorChangesAdapter.class,
                        AddEntityPacketAdapter.class,
                        EntityMetadataPacketAdapter.class,
                        PlayerRotationPacketAdapter.class,
                        ClientBundlePacketAdapter.class,
                        SetEquipmentPacketAdapter.class,
                        AdventureAdapter.class,
                        PlayerPositionPacketAdapter.class
                ),
                plugin,
                entityTypeAdapter,
                majorChangesAdapter,
                addEntityPacketAdapter,
                entityMetadataPacketAdapter,
                playerRotationPacketAdapter,
                clientBundlePacketAdapter,
                setEquipmentPacketAdapter,
                adventureAdapter,
                playerPositionPacketAdapter
        );
    }


}
