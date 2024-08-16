package it.jakegblp.lusk.elements.minecraft.blocks.loom.types;

import ch.njol.skript.Skript;
import ch.njol.skript.bukkitutil.BukkitUtils;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.registrations.Classes;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import it.jakegblp.lusk.utils.PaperUtils;
import it.jakegblp.lusk.wrappers.EnumWrapper;
import it.jakegblp.lusk.wrappers.RegistryClassInfo;
import org.bukkit.Registry;
import org.bukkit.block.banner.PatternType;


public class Types {
    static {
        if (Skript.classExists("org.bukkit.block.banner.PatternType") && Classes.getExactClassInfo(PatternType.class) == null) {
            Registry<PatternType> patternTypeRegistry = null;
            if (PaperUtils.registryExists("BANNER_PATTERN")) {
                patternTypeRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN);
            } else if (BukkitUtils.registryExists("BANNER_PATTERN")) {
                patternTypeRegistry = Registry.BANNER_PATTERN;
            }
            ClassInfo<PatternType> patternTypeClassInfo;
            if (patternTypeRegistry != null) {
                Skript.info("\nregistry exists\n");
                patternTypeClassInfo = RegistryClassInfo.create(patternTypeRegistry, PatternType.class, true, "patterntype", null, "pattern");
            } else {
                patternTypeClassInfo = new EnumWrapper<>((Class)PatternType.class,null,"pattern").getClassInfo("patterntype");
                Skript.info("\nregistry doesn't exist, using enum\n");
            }
            Classes.registerClass(patternTypeClassInfo
                    .user("pattern ?types?")
                    .name("Banner - Pattern Type")
                    .description("Represents all the available banner pattern types.")
                    .since("1.0.0, 1.2.1 (Registries)")
            );
        }
    }
}
