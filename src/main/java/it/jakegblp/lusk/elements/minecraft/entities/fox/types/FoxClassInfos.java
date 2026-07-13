package it.jakegblp.lusk.elements.minecraft.entities.fox.types;

import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumRegistryWrapper;
import org.bukkit.entity.Fox;

@SuppressWarnings("unused")
public class FoxClassInfos {
    static {
        if (Classes.getExactClassInfo(Fox.Type.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Fox.Type.class, null, "fox_type")
                            .getClassInfo("foxtype")
                            .user("fox ?(variant|colou?r|type)s?")
                            .name("Fox - Type")
                            .description("All the Fox Types.")
                            .since("1.3")
                            .documentationId("FoxType"));
        }

    }
}
