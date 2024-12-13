package it.jakegblp.lusk.elements.minecraft.entities.fox.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumRegistryWrapper;
import org.bukkit.entity.Fox;

@SuppressWarnings("unused")
public class FoxClassInfos {
    static {
        if (Skript.classExists("org.bukkit.entity.Fox$Type") && Classes.getExactClassInfo(Fox.Type.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Fox.Type.class, null, "fox_type")
                            .getClassInfo("foxtype")
                            .user("fox ?(variant|colou?r|type)s?")
                            .description("All the Fox Types.")
                            .since("1.3"));
        }

    }
}
