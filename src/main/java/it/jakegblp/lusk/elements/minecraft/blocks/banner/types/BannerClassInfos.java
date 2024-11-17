package it.jakegblp.lusk.elements.minecraft.blocks.banner.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumRegistryWrapper;
import org.bukkit.block.banner.PatternType;

@SuppressWarnings("unused")
public class BannerClassInfos {
    static {
        if (Skript.classExists("org.bukkit.block.banner.PatternType") && Classes.getExactClassInfo(PatternType.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(PatternType.class, null, "pattern")
                            .getClassInfo("patterntype")
                            .user("pattern ?types?")
                            .name("Banner - Pattern Type")
                            .description("Represents all the available banner pattern types.")
                            .documentationId("8850")
                            .since("1.0.0, 1.2.1 (Registries), 1.3 (1.20.4 Fix)"));
        }
    }
}
