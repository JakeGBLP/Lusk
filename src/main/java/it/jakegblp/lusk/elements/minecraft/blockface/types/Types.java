package it.jakegblp.lusk.elements.minecraft.blockface.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.wrappers.EnumWrapper;
import org.bukkit.block.BlockFace;

public class Types {
    static {
        if (Skript.classExists("org.bukkit.block.BlockFace") && Classes.getExactClassInfo(BlockFace.class) == null) {
            EnumWrapper<BlockFace> BLOCKFACE_ENUM = new EnumWrapper<>(BlockFace.class, null, "face");
            Classes.registerClass(BLOCKFACE_ENUM.getClassInfo("blockface")
                    .user("block ?faces?")
                    .name("Block Face")
                    .description("All the Block Faces.")
                    .examples("south blockface","up blockface")
                    .since("1.1"));
        }
    }
}
