package it.jakegblp.lusk.utils;

import com.vdurmont.semver4j.Semver;
import it.jakegblp.lusk.api.NMSAdapter;
import it.jakegblp.lusk.api.SkriptAdapter;
import it.jakegblp.lusk.version.nms.v1_21_3.NMS_1_21_3;
import it.jakegblp.lusk.version.nms.v1_21_4.NMS_1_21_4;
import it.jakegblp.lusk.version.skript.v2_10_0.Skript_2_10_0;
import it.jakegblp.lusk.version.skript.v2_6_4.Skript_2_6_4;
import it.jakegblp.lusk.version.skript.v2_7_3.Skript_2_7_3;
import it.jakegblp.lusk.version.skript.v2_8_7.Skript_2_8_7;
import it.jakegblp.lusk.version.skript.v2_9_5.Skript_2_9_5;
import it.jakegblp.lusk.versions.nms.v1_16_5.NMS_1_16_5;
import it.jakegblp.lusk.versions.nms.v1_17.NMS_1_17;
import it.jakegblp.lusk.versions.nms.v1_17_1.NMS_1_17_1;
import it.jakegblp.lusk.versions.nms.v1_18.NMS_1_18;
import it.jakegblp.lusk.versions.nms.v1_18_1.NMS_1_18_1;
import it.jakegblp.lusk.versions.nms.v1_18_2.NMS_1_18_2;
import it.jakegblp.lusk.versions.nms.v1_19.NMS_1_19;
import it.jakegblp.lusk.versions.nms.v1_19_1.NMS_1_19_1;
import it.jakegblp.lusk.versions.nms.v1_19_3.NMS_1_19_3;
import it.jakegblp.lusk.versions.nms.v1_19_4.NMS_1_19_4;
import it.jakegblp.lusk.versions.nms.v1_20_1.NMS_1_20_1;
import it.jakegblp.lusk.versions.nms.v1_20_2.NMS_1_20_2;
import it.jakegblp.lusk.versions.nms.v1_20_4.NMS_1_20_4;
import it.jakegblp.lusk.versions.nms.v1_20_6.NMS_1_20_6;
import it.jakegblp.lusk.versions.nms.v1_21_1.NMS_1_21_1;

public class VersionResolver {

    public static SkriptAdapter resolveSkriptAdapter(Semver version) {
        int major = version.getMajor(),
            minor = version.getMinor(),
            patch = version.getPatch();
        SkriptAdapter skriptAdapter = switch (major) {
            case 2 -> switch (minor) {
                case 6 -> new Skript_2_6_4();
                case 7 -> new Skript_2_7_3();
                case 8 -> new Skript_2_8_7();
                case 9 -> new Skript_2_9_5();
                case 10 -> new Skript_2_10_0();
                default -> null;
            };
            default -> null;
        };
        return skriptAdapter;
    }

    public static NMSAdapter resolveNMSAdapter(Semver version) {
        int major = version.getMajor(),
                minor = version.getMinor(),
                patch = version.getPatch();
        return switch (major) {
            case 1 -> switch (minor) {
                case 16 -> switch (patch) {
                    case 5 -> new NMS_1_16_5();
                    default -> null;
                };
                case 17 -> switch (patch) {
                    case 0 -> new NMS_1_17();
                    case 1 -> new NMS_1_17_1();
                    default -> null;
                };
                case 18 -> switch (patch) {
                    case 0 -> new NMS_1_18();
                    case 1 -> new NMS_1_18_1();
                    case 2 -> new NMS_1_18_2();
                    default -> null;
                };
                case 19 -> switch (patch) {
                    case 0 -> new NMS_1_19();
                    case 1, 2 -> new NMS_1_19_1();
                    case 3 -> new NMS_1_19_3();
                    case 4 -> new NMS_1_19_4();
                    default -> null;
                };
                case 20 -> switch (patch) {
                    case 0, 1 -> new NMS_1_20_1();
                    case 2 -> new NMS_1_20_2();
                    case 3, 4 -> new NMS_1_20_4();
                    case 5, 6 -> new NMS_1_20_6();
                    default -> null;
                };
                case 21 -> switch (patch) {
                    case 0, 1 -> new NMS_1_21_1();
                    case 2, 3 -> new NMS_1_21_3();
                    case 4 -> new NMS_1_21_4();
                    default -> null;
                };
                default -> null;
            };
            default -> null;
        };
    }

}