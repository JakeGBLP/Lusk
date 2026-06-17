package it.jakegblp.lusk.nms.factory;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.impl.allversions.AllVersions;
import it.jakegblp.lusk.nms.impl.from_1_21_11.From_1_21_11;
import it.jakegblp.lusk.nms.impl.from_1_21_3.From_1_21_3;
import it.jakegblp.lusk.nms.impl.from_1_21_5_to_1_21_10.From_1_21_5_To_1_21_10;
import it.jakegblp.lusk.nms.impl.to_1_21_1.To_1_21_1;
import it.jakegblp.lusk.nms.impl.to_1_21_10.To_1_21_10;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static it.jakegblp.lusk.common.Version.of;

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
        var allVersions = new AllVersions();
        var mappings = allVersions.getMappings();
        var codecs = allVersions.getCodecs();
        nmsBuilder.setSharedBehaviorAdapter(allVersions);
        nmsBuilder.setAbstractNMSConsumer(nms -> {
            if (minecraftVersion.isGreaterOrEqual(of(1, 21, 3)))
                From_1_21_3.registerCodecs(mappings, codecs);
            else
                To_1_21_1.registerCodecs(mappings, codecs);
            if (minecraftVersion.isGreaterOrEqual(of(1, 21, 5)) && minecraftVersion.isLowerOrEqual(of(1, 21, 10)))
                From_1_21_5_To_1_21_10.registerCodecs(mappings, codecs);
            if (minecraftVersion.isGreaterOrEqual(of(1, 21, 11))) {
                From_1_21_11.registerCodecs(mappings, codecs);
                System.out.println("DONE, DID THAT");
            }
            else
                To_1_21_10.registerCodecs(mappings, codecs);
        });
        return nmsBuilder.build();
    }
}
