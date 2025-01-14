package it.jakegblp.lusk.elements.minecraft.entities.rabbit.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumRegistryWrapper;
import org.bukkit.entity.Rabbit;

@SuppressWarnings("unused")
public class RabbitClassInfos {
    static {
        if (Skript.classExists("org.bukkit.entity.Rabbit$Type") && Classes.getExactClassInfo(Rabbit.Type.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(Rabbit.Type.class, null, "rabbit_type")
                            .getClassInfo("rabbittype")
                            .user("rabbit ?(variant|type)s?")
                            .description("All the Rabbit Types.")
                            .since("1.3")
                            .documentationId("RabbitType"));
        }

    }
}
