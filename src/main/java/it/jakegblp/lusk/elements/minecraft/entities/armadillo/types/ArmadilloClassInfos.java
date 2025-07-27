package it.jakegblp.lusk.elements.minecraft.entities.armadillo.types;

import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumWrapper;
import org.bukkit.entity.Armadillo;

import static it.jakegblp.lusk.utils.Constants.MINECRAFT_1_21_5;

@SuppressWarnings("unused")
public class ArmadilloClassInfos {
    static {
        if (MINECRAFT_1_21_5) {
            EnumWrapper<Armadillo.State> ARMOR_STAND_INTERACTION_ENUM = new EnumWrapper<>(Armadillo.State.class);
            Classes.registerClass(ARMOR_STAND_INTERACTION_ENUM.getClassInfo("armadillostate")
                    .user("armadillo ?states?")
                    .name("Armadillo State")
                    .description("All the Armadillo States.") // add example
                    .since("1.3.8"));
        }
    }
}
