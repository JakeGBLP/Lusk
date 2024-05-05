package it.jakegblp.lusk.elements.minecraft.blocks.loom.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.wrappers.EnumWrapper;
import org.bukkit.block.banner.PatternType;

public class Types {
    static {
        if (Skript.classExists("org.bukkit.block.banner.PatternType") && Classes.getExactClassInfo(PatternType.class) == null) {
            EnumWrapper<PatternType> PATTERNTYPE_ENUM = new EnumWrapper<>(PatternType.class);
            Classes.registerClass(PATTERNTYPE_ENUM.getClassInfo("patterntype")
                    .user("pattern ?types?")
                    .name("Pattern Type")
                    .description("All the Pattern Types.") // add example
                    .since("1.0.0"));
        }
    }
}
