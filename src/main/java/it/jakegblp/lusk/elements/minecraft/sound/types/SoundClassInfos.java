package it.jakegblp.lusk.elements.minecraft.sound.types;

import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumRegistryWrapper;
import org.bukkit.Sound;

@SuppressWarnings("unused")
public class SoundClassInfos {
    static {
        if (Classes.getExactClassInfo(Sound.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Sound.class, null, "sound")
                            .getClassInfo("sound")
                            .user("sounds?")
                            .name("Sound")
                            .description("All the sounds, this varies across versions, some sounds might be removed.")
                            .since("1.3"));
        }
    }
}
