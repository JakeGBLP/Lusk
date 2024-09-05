package it.jakegblp.lusk.utils;

import ch.njol.skript.Skript;
import ch.njol.util.coll.BidiHashMap;
import ch.njol.util.coll.BidiMap;
import com.vdurmont.semver4j.Semver;
import org.bukkit.Rotation;

import java.util.HashMap;

import static it.jakegblp.lusk.utils.LuskUtils.Version;

public class Constants {
    public static final Semver
            serverVersion = Version(String.valueOf(Skript.getMinecraftVersion())),
            skriptVersion = Version(Skript.getVersion().toString());

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
        put(767, Version("1.21.1"));
    }};

    public static final BidiMap<Rotation, Integer> itemFrameRotations = new BidiHashMap<>() {{
        put(Rotation.NONE, 0);
        put(Rotation.CLOCKWISE_45, 45);
        put(Rotation.CLOCKWISE, 90);
        put(Rotation.CLOCKWISE_135, 135);
        put(Rotation.FLIPPED, 180);
        put(Rotation.FLIPPED_45, 225);
        put(Rotation.COUNTER_CLOCKWISE, 270);
        put(Rotation.COUNTER_CLOCKWISE_45, 315);
    }};
}
