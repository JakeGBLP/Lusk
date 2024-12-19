package it.jakegblp.lusk.elements.minecraft.blockface.types;

import ch.njol.skript.Skript;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.skript.EnumWrapper;
import org.bukkit.block.BlockFace;

@SuppressWarnings("unused")
public class BlockFaceClassInfos {
    static {
        if (Skript.classExists("org.bukkit.block.BlockFace") && Classes.getExactClassInfo(BlockFace.class) == null) {
            EnumWrapper<BlockFace> BLOCKFACE_ENUM = new EnumWrapper<>(BlockFace.class, null, "face");
            Classes.registerClass(BLOCKFACE_ENUM.getClassInfo("blockface")
                    .user("(block ?)?faces?")
                    .name("Block Face")
                    .description("All the Block Faces.")
                    .examples("south face", "up face")
                    .documentationId("11206")
                    .since("1.1"));
        }
    }
}
