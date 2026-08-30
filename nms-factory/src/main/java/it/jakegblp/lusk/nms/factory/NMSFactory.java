package it.jakegblp.lusk.nms.factory;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.impl.allversions.AllVersions;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class NMSFactory {

    public static AbstractNMS createNMS(
            JavaPlugin javaPlugin,
            @NotNull Version minecraftVersion
    ) {
        int
                major = minecraftVersion.major(),
                minor = minecraftVersion.minor(),
                patch = minecraftVersion.patch();
        if (major == 1 && minor == 21) {
            switch (patch) {
                case 0, 2, 7, 9 -> patch++;
            }
        }

        NMSBuilder nmsBuilder = new NMSBuilder(major, minor, patch);
        nmsBuilder.setPlugin(javaPlugin);
        nmsBuilder.setSharedBehaviorAdapter(new AllVersions());
        return nmsBuilder.build();
    }
}
