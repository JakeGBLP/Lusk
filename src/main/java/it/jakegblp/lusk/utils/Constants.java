package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import com.vdurmont.semver4j.Semver;
import org.bukkit.Rotation;

import java.util.HashMap;

import static it.jakegblp.lusk.utils.Utils.Version;

public class Constants {
    public static final Semver serverVersion = Version(String.valueOf(Skript.getMinecraftVersion()));
    public static final HashMap<Integer, Semver> versions = new HashMap<>() {{
        put(4, Version("1.7.5"));
        put(5, Version("1.7.10"));
        put(47, Version("1.8.9"));
        put(107, Version("1.9"));
        put(108, Version("1.9.1"));
        put(109, Version("1.9.2"));
        put(110, Version("1.9.4"));
        put(210, Version("1.10.2"));
        put(315, Version("1.11.0"));
        put(316, Version("1.11.2"));
        put(335, Version("1.12.0"));
        put(338, Version("1.12.1"));
        put(340, Version("1.12.2"));
        put(393, Version("1.13"));
        put(401, Version("1.13.1"));
        put(404, Version("1.13.2"));
        put(477, Version("1.14.0"));
        put(480, Version("1.14.1"));
        put(485, Version("1.14.2"));
        put(490, Version("1.14.3"));
        put(498, Version("1.14.4"));
        put(573, Version("1.15"));
        put(575, Version("1.15.1"));
        put(577, Version("1.15.2"));
        put(735, Version("1.16.0"));
        put(736, Version("1.16.1"));
        put(751, Version("1.16.2"));
        put(753, Version("1.16.3"));
        put(754, Version("1.16.5"));
        put(755, Version("1.17"));
        put(756, Version("1.17.1"));
        put(757, Version("1.18.1"));
        put(758, Version("1.18.2"));
        put(759, Version("1.19"));
        put(760, Version("1.19.2"));
        put(761, Version("1.19.3"));
        put(762, Version("1.19.4"));
        put(763, Version("1.20.1"));
        put(764, Version("1.20.2"));
        put(765, Version("1.20.4"));
        put(766, Version("1.20.6"));
        put(767, Version("1.21"));
    }};

    public static final Semver skriptVersion = Version(Skript.getVersion().toString());
    public static final HashMap<Integer, Rotation> itemFrameRotations = new HashMap<>() {{
        put(0, Rotation.NONE);
        put(45, Rotation.CLOCKWISE_45);
        put(90, Rotation.CLOCKWISE);
        put(135, Rotation.CLOCKWISE_135);
        put(180, Rotation.FLIPPED);
        put(225, Rotation.FLIPPED_45);
        put(270, Rotation.COUNTER_CLOCKWISE);
        put(315, Rotation.COUNTER_CLOCKWISE_45);
    }};
}
