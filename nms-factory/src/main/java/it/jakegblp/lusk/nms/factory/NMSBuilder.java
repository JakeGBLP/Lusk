package it.jakegblp.lusk.nms.factory;

import it.jakegblp.lusk.common.reflection.SimpleClass;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.core.adapter.SharedBehaviorAdapter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

@Setter
public class NMSBuilder {

    protected final int major, minor, patch;
    protected JavaPlugin plugin;
    @SuppressWarnings("rawtypes")
    protected SharedBehaviorAdapter sharedBehaviorAdapter;

    public NMSBuilder(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public AbstractNMS build() {
        String version = "v" + major + "_" + minor + (patch == 0 ? "" : "_" + patch);
        return (AbstractNMS) SimpleClass.of("it.jakegblp.lusk.nms.impl." + version + "." + version).getConstructor(
                JavaPlugin.class, SharedBehaviorAdapter.class)
                .newInstance(plugin, sharedBehaviorAdapter);
    }

}
