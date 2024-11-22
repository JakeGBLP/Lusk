package it.jakegblp.lusk.elements.minecraft.blocks.block.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.bukkit.block.BlockState;
import org.jetbrains.annotations.NotNull;

public class BlockClassInfos {
    static {
        if (Classes.getExactClassInfo(BlockState.class) == null) {
            Classes.registerClass(new ClassInfo<>(BlockState.class, "blockstate")
                    .user("block ?states?")
                    .name("BlockState")
                    .description("""
                    Represents a captured state of a block, which will not change automatically.
                    Unlike Block, which only one object can exist per coordinate, BlockState can exist multiple times for any given Block. Note that another plugin may change the state of the block and you will not know, or they may change the block to another type entirely, causing your BlockState to become invalid.
                    """)
                    .since("1.3")
                    .parser(new Parser<>() {
                        @Override
                        public boolean canParse(ParseContext context) {
                            return false;
                        }

                        @Override
                        public @NotNull String toString(BlockState blockState, int flags) {
                            return (blockState.isPlaced() ? "placed " : "") +"BlockState of type '"+blockState.getType()+"' at '"+blockState.getLocation()+'"';
                        }

                        @Override
                        public @NotNull String toVariableNameString(BlockState blockState) {
                            return toString(blockState, 0);
                        }
                    }));
        }
    }
}
