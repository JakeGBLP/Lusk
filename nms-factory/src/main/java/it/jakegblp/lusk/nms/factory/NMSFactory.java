package it.jakegblp.lusk.nms.factory;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.nms.core.AbstractNMS;
import it.jakegblp.lusk.nms.impl.allversions.AllVersions;
import it.jakegblp.lusk.nms.impl.from_1_20_6.From_1_20_6;
import it.jakegblp.lusk.nms.impl.from_1_21_3.From_1_21_3;
import it.jakegblp.lusk.nms.impl.to_1_20_4.To_1_20_4;
import it.jakegblp.lusk.nms.impl.to_1_21_1.To_1_21_1;
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
                case 20 -> {
                    switch (patch) {
                        case 0, 3, 5 -> patch++;
                    }
                }
                case 21 -> {
                    switch (patch) {
                        case 0, 2, 7, 9 -> patch++;
                    }
                }
            }
        }

        NMSBuilder nmsBuilder = new NMSBuilder(major, minor, patch);
        nmsBuilder.setPlugin(javaPlugin);

        if (minecraftVersion.isGreaterOrEqual(of(1, 21, 3))) {
            var from_1_21_3 = new From_1_21_3();
            nmsBuilder.setPlayerRotationPacketAdapter(from_1_21_3);
            nmsBuilder.setPlayerPositionPacketAdapter(from_1_21_3);
        } else
            nmsBuilder.setPlayerPositionPacketAdapter(new To_1_21_1());

        if (minecraftVersion.isGreaterOrEqual(of(1, 20, 6)))
            nmsBuilder.setSetEquipmentPacketAdapter(new From_1_20_6());
        else
            nmsBuilder.setSetEquipmentPacketAdapter(new To_1_20_4());

        nmsBuilder.setSharedBehaviorAdapter(new AllVersions());

        return nmsBuilder.build();
    }
}
