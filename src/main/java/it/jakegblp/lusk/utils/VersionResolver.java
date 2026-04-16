package it.jakegblp.lusk.utils;

import com.vdurmont.semver4j.Semver;
import it.jakegblp.lusk.api.SkriptAdapter;
import it.jakegblp.lusk.version.skript.v2_10_0.Skript_2_10_0;
import it.jakegblp.lusk.version.skript.v2_15_0.Skript_2_15_0;
import org.skriptlang.skript.addon.SkriptAddon;

public class VersionResolver {

    public static SkriptAdapter getSkriptAdapter(Semver version, SkriptAddon skriptAddon) {
        int major = version.getMajor(),
            minor = version.getMinor(),
            patch = version.getPatch();
        SkriptAdapter skriptAdapter = switch (major) {
            case 2 -> minor >= 15 ? new Skript_2_15_0(skriptAddon) : new Skript_2_10_0();
            default -> null;
        };
        return skriptAdapter;
    }

    //public static MinecraftAdapter getMinecraftAdapter(Semver version) {
    //    int major = version.getMajor(),
    //            minor = version.getMinor(),
    //            patch = version.getPatch();
    //    return switch (major) {
    //        case 1 -> switch (minor) {
    //            case 20 -> new Minecraft_1_20();
    //            case 21 -> new Minecraft_1_21();
    //            default -> new Minecraft_1_21();
    //        };
    //        default -> new Minecraft_1_21();
    //    };
    //}

}