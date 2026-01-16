package it.jakegblp.lusk.nms.factory;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.impl.allversions.AllVersions;
import it.jakegblp.lusk.nms.impl.allversions.BiomeAllVersions;
import it.jakegblp.lusk.nms.impl.from_1_21_3.From_1_21_3;
import it.jakegblp.lusk.nms.impl.to_1_21_1.To_1_21_1;
import org.bukkit.plugin.java.JavaPlugin;

import static it.jakegblp.lusk.common.Version.of;


public class NMSFactory {

    public static AbstractNMS createNMS(
            JavaPlugin javaPlugin,
            Version minecraftVersion
    ) {
        int
                major = minecraftVersion.major(),
                minor = minecraftVersion.minor(),
                patch = minecraftVersion.patch();
        if (major == 1) {
            if (minor == 21) {
                switch (patch) {
                    case 0, 2, 7, 9 -> patch++;
                }
            }
        }

        NMSBuilder nmsBuilder = new NMSBuilder(major, minor, patch);
        nmsBuilder.setPlugin(javaPlugin);
        nmsBuilder.setSharedBehaviorAdapter(new AllVersions());
        nmsBuilder.setSharedBiomeAdapter(new BiomeAllVersions()); // todo: spread this across the appropriate versions
        var nms = nmsBuilder.build();

        if (minecraftVersion.isGreaterOrEqual(of(1, 21, 3)))
            From_1_21_3.registerCodecs(nms);
        else
            To_1_21_1.registerCodecs(nms);


        return nms;
    }
}
