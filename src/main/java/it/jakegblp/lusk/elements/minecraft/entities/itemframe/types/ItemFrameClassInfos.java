package it.jakegblp.lusk.elements.minecraft.entities.itemframe.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumSerializer;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.EnumUtils;
import org.bukkit.Rotation;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class ItemFrameClassInfos {
    static {
        if (Classes.getExactClassInfo(Rotation.class) == null) {
            /*
                One of the values of this enum is "NONE" which can conflict with other things.
                Implementing Rotation without taking notice of this is an oversight and must not be done.
                For this reason, I've decided to register this enum the "original" way and add lang entries
                 to make each element make more sense and be more versatile.
             */
            EnumUtils<Rotation> ROTATION_ENUM = new EnumUtils<>(Rotation.class, "rotations");
            Classes.registerClass(new ClassInfo<>(Rotation.class, "rotation")
                    .user("rotations?")
                    .name("Rotations")
                    .description("All the regular Rotations.\nRotations are used in item frames.") // add example
                    .usage(ROTATION_ENUM.getAllNames())
                    .since("1.3")
                    .parser(new Parser<>() {
                        @Override
                        @Nullable
                        public Rotation parse(final String input, final ParseContext context) {
                            return ROTATION_ENUM.parse(input);
                        }

                        @Override
                        public String toString(Rotation rotation, int flags) {
                            return ROTATION_ENUM.toString(rotation, flags);
                        }

                        @SuppressWarnings("null")
                        @Override
                        public String toVariableNameString(Rotation rotation) {
                            return rotation.name();
                        }

                    })
                    .serializer(new EnumSerializer<>(Rotation.class)));
        }
    }
}
