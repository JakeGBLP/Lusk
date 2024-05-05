package it.jakegblp.lusk.elements.minecraft.boundingbox.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class Types {
    static {
        if (Classes.getExactClassInfo(BoundingBox.class) == null)
            Classes.registerClass(new ClassInfo<>(BoundingBox.class, "boundingbox")
                    .user("bounding ?box(es)?")
                    .name("Bounding Box")
                    .description("A Bounding Box") // add example
                    .since("1.0.2")
                    .parser(new Parser<>() {
                        @Override
                        @Nullable
                        public BoundingBox parse(final @NotNull String s, final @NotNull ParseContext context) {
                            return null;
                        }

                        @Override
                        public boolean canParse(final @NotNull ParseContext context) {
                            return false;
                        }

                        @Override
                        public @NotNull String toString(final BoundingBox b, final int flags) {
                            return b.toString();
                        }

                        @Override
                        public @NotNull String toVariableNameString(final BoundingBox b) {
                            return b.toString();
                        }

                        @Override
                        public @NotNull String getDebugMessage(final BoundingBox b) {
                            return toString(b, 0) + " bounding box (" + b + ")";
                        }
                    }));
    }
}
