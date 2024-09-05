package it.jakegblp.lusk.elements.minecraft.boundingbox.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.StreamCorruptedException;
import java.util.stream.Collectors;

import static it.jakegblp.lusk.utils.LuskUtils.toSkriptConfigNumberAccuracy;

public class Types {
    static {
        if (Classes.getExactClassInfo(BoundingBox.class) == null)
            Classes.registerClass(new ClassInfo<>(BoundingBox.class, "boundingbox")
                    .user("bounding ?box(es)?")
                    .name("Bounding Box")
                    .description("A Bounding Box.\n\nCan be saved in global variables since 1.2") // add example
                    .since("1.0.2, 1.2 (Savable)")
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
                            return toSkriptConfigNumberAccuracy(b.toString());
                        }

                        @Override
                        public @NotNull String toVariableNameString(final BoundingBox b) {
                            return b.toString();
                        }

                        @Override
                        public @NotNull String getDebugMessage(final BoundingBox b) {
                            return toString(b, 0) + " bounding box (" + b + ")";
                        }
                    })
                    .serializer(new Serializer<>() {
                        @Override
                        public @NotNull Fields serialize(BoundingBox o) {
                            Fields f = new Fields();
                            f.putPrimitive("minX", o.getMinX());
                            f.putPrimitive("minY", o.getMinY());
                            f.putPrimitive("minZ", o.getMinZ());
                            f.putPrimitive("maxX", o.getMaxX());
                            f.putPrimitive("maxY", o.getMaxY());
                            f.putPrimitive("maxZ", o.getMaxZ());
                            return f;
                        }

                        @Override
                        public void deserialize(BoundingBox o, @NotNull Fields f) {
                            assert false;
                        }

                        @Override
                        protected BoundingBox deserialize(@NotNull Fields f) throws StreamCorruptedException {
                            return new BoundingBox(
                                    f.getPrimitive("minX", double.class),
                                    f.getPrimitive("minY", double.class),
                                    f.getPrimitive("minZ", double.class),
                                    f.getPrimitive("maxX", double.class),
                                    f.getPrimitive("maxY", double.class),
                                    f.getPrimitive("maxZ", double.class)
                            );

                        }

                        @Override
                        public boolean mustSyncDeserialization() {
                            return true;
                        }

                        @Override
                        protected boolean canBeInstantiated() {
                            return false;
                        }
                    }));
        if (Classes.getExactClassInfo(VoxelShape.class) == null)
            Classes.registerClass(new ClassInfo<>(VoxelShape.class, "voxelshape")
                    .user("voxel ?shapes?")
                    .name("Voxel Shape")
                    .description("A Voxel Shape, usually used for blocks such as Stairs.\n\nYou can store those but it's not persistent across restarts.") // add example
                    .since("1.2")
                    .parser(new Parser<>() {
                        @Override
                        @Nullable
                        public VoxelShape parse(final @NotNull String s, final @NotNull ParseContext context) {
                            return null;
                        }

                        @Override
                        public boolean canParse(final @NotNull ParseContext context) {
                            return false;
                        }

                        @Override
                        public @NotNull String toString(final VoxelShape s, final int flags) {
                            return "VoxelShape [" + (s.getBoundingBoxes().stream().map(box -> toSkriptConfigNumberAccuracy(box.toString())).collect(Collectors.joining(", ")));
                        }

                        @Override
                        public @NotNull String toVariableNameString(final VoxelShape s) {
                            return s.toString();
                        }

                        @Override
                        public @NotNull String getDebugMessage(final VoxelShape s) {
                            return toString(s, 0) + " voxel shape (" + s + ")";
                        }
                    }));
    }
}
