package it.jakegblp.lusk.nms.factory;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.impl.allversions.AllVersions;
import it.jakegblp.lusk.nms.impl.from_1_18.From_1_18;
import it.jakegblp.lusk.nms.impl.from_1_18_to_1_18_2.From_1_18_To_1_18_2;
import it.jakegblp.lusk.nms.impl.from_1_18_to_1_19_1.From_1_18_To_1_19_1;
import it.jakegblp.lusk.nms.impl.from_1_18_to_1_20_4.From_1_18_To_1_20_4;
import it.jakegblp.lusk.nms.impl.from_1_19.From_1_19;
import it.jakegblp.lusk.nms.impl.from_1_19_3.From_1_19_3;
import it.jakegblp.lusk.nms.impl.from_1_19_4.From_1_19_4;
import it.jakegblp.lusk.nms.impl.from_1_19_4_to_1_21_1.From_1_19_4_To_1_21_1;
import it.jakegblp.lusk.nms.impl.from_1_20_6.From_1_20_6;
import it.jakegblp.lusk.nms.impl.from_1_21_3.From_1_21_3;
import it.jakegblp.lusk.nms.impl.to_1_17_1.To_1_17_1;
import org.bukkit.plugin.java.JavaPlugin;

import static it.jakegblp.lusk.common.Version.of;


public class NMSFactory {

    public static AbstractNMS<?> createNMS(
            JavaPlugin javaPlugin,
            Version minecraftVersion
    ) {
        int
                major = minecraftVersion.major(),
                minor = minecraftVersion.minor(),
                patch = minecraftVersion.patch();
        if (major == 1) {
            switch (minor) {
                case 19 -> {
                    if (patch == 2) patch = 1;
                }
                case 20 -> {
                    switch (patch) {
                        case 0 -> patch = 1;
                        case 3 -> patch = 4;
                        case 5 -> patch = 6;
                    }
                }
                case 21 -> {
                    switch (patch) {
                        case 0 -> patch = 1;
                        case 2 -> patch = 3;
                    }
                }
            }
        }

        NMSBuilder nmsBuilder = new NMSBuilder(major, minor, patch);
        nmsBuilder.setPlugin(javaPlugin);
        To_1_17_1 to_1_17_1;
        boolean atLeast_1_18 = minecraftVersion.isGreaterOrEqual(of(1, 18));
        // Conversion Adapter:
        // <= 1.17.1
        // >= 1.18
        if (atLeast_1_18)
            nmsBuilder.setMajorChangesAdapter(new From_1_18());
        else {
            to_1_17_1 = new To_1_17_1();
            nmsBuilder.setMajorChangesAdapter(to_1_17_1);
            nmsBuilder.setEntityTypeAdapter(to_1_17_1);
            nmsBuilder.setEntityMetadataPacketAdapter(to_1_17_1);
            nmsBuilder.setAddEntityPacketAdapter(to_1_17_1);
            nmsBuilder.setSetEquipmentPacketAdapter(to_1_17_1);
            nmsBuilder.setPlayerPositionPacketAdapter(to_1_17_1);
        }

        if (minecraftVersion.isGreaterOrEqual(of(1, 19, 3))) {
            var from_1_19_3 = new From_1_19_3();
            nmsBuilder.setEntityTypeAdapter(from_1_19_3);
            nmsBuilder.setEntityMetadataPacketAdapter(from_1_19_3);
        } else if (atLeast_1_18) {
            var from_1_18_To_1_19_1 = new From_1_18_To_1_19_1();
            nmsBuilder.setEntityTypeAdapter(from_1_18_To_1_19_1);
            nmsBuilder.setEntityMetadataPacketAdapter(from_1_18_To_1_19_1);
        }

        if (minecraftVersion.isGreaterOrEqual(of(1, 19)))
            nmsBuilder.setAddEntityPacketAdapter(new From_1_19());
        else if (atLeast_1_18)
            nmsBuilder.setAddEntityPacketAdapter(new From_1_18_To_1_18_2());

        if (minecraftVersion.isGreaterOrEqual(of(1, 21, 3))) {
            var from_1_21_3 = new From_1_21_3();
            nmsBuilder.setPlayerRotationPacketAdapter(from_1_21_3);
            nmsBuilder.setPlayerPositionPacketAdapter(from_1_21_3);
        } else if (atLeast_1_18)
            nmsBuilder.setPlayerPositionPacketAdapter(new From_1_19_4_To_1_21_1());

        if (minecraftVersion.isGreaterOrEqual(of(1, 19, 4)))
            nmsBuilder.setClientBundlePacketAdapter(new From_1_19_4());

        if (minecraftVersion.isGreaterOrEqual(of(1, 20, 6)))
            nmsBuilder.setSetEquipmentPacketAdapter(new From_1_20_6());
        else if (atLeast_1_18)
            nmsBuilder.setSetEquipmentPacketAdapter(new From_1_18_To_1_20_4());

        nmsBuilder.setAdventureAdapter(new AllVersions());

        return nmsBuilder.build();
    }
}
