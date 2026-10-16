package it.jakegblp.lusk.skript.modules.boundingbox;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.common.Instances;
import it.jakegblp.lusk.skript.api.module.LuskModule;
import it.jakegblp.lusk.skript.modules.base.BaseModule;
import it.jakegblp.lusk.skript.modules.boundingbox.conditions.CondBoundingBoxOverlaps;
import it.jakegblp.lusk.skript.modules.boundingbox.expressions.*;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.StructurePiece;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.bukkit.util.VoxelShape;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.lang.converter.Converters;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.handlers.ContainsHandler;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.io.StreamCorruptedException;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

public class BoundingBoxModule extends LuskModule {

    public static final Property<ExpressionPropertyHandler<?, BoundingBox>> BOUNDING_BOX = Property.of(
            "bounding box",
            "The bounding box of an object.",
            "2.0.0",
            Instances.SKRIPT_ADDON,
            ExpressionPropertyHandler.class
    );

    public static final Property<ExpressionPropertyHandler<?, Object>> BOUNDING_BOXES = Property.of(
            "bounding boxes",
            "The bounding boxes of an object.",
            "2.0.0",
            Instances.SKRIPT_ADDON,
            ExpressionPropertyHandler.class
    );

    public static final Property<ExpressionPropertyHandler<?, VoxelShape>> VOXEL_SHAPE = Property.of(
            "voxel shape",
            "The voxel shape of an object.",
            "2.0.0",
            Instances.SKRIPT_ADDON,
            ExpressionPropertyHandler.class
    );

    public BoundingBoxModule(IntConsumer adder) {
        super(adder);
    }

    @Override
    protected void registration(SyntaxRegistry registry) {
        CondBoundingBoxOverlaps.register(registry);

        ExprBoundingBoxBlocks.register(registry);
        ExprBoundingBoxCenter.register(registry);
        ExprBoundingBoxCoordinates.register(registry);
        ExprBoundingBoxCorners.register(registry);
        ExprBoundingBoxEntities.register(registry);
        ExprBoundingBoxMeasurements.register(registry);
        PropExprBoundingBox.register(registry);
        PropExprBoundingBoxes.register(registry);
        PropExprVoxelShape.register(registry);
    }

    @Override
    public void init(SkriptAddon addon) {
        if (Classes.getExactClassInfo(BoundingBox.class) == null) {
            Classes.registerClass(new ClassInfo<>(BoundingBox.class, "boundingbox")
                    .user("bounding ?box(es)?")
                    .name("Bounding Box")
                    .description("A Bounding Box.")
                    .since("1.0.2, 1.2 (Savable in variables)")
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
                        public @NotNull String toString(final @NotNull BoundingBox b, final int flags) {
                            return AddonUtils.withSkriptNumberAccuracy(b.toString());
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
            Converters.registerConverter(Block.class, BoundingBox.class, Block::getBoundingBox);
            Converters.registerConverter(Entity.class, BoundingBox.class, Entity::getBoundingBox);
            Converters.registerConverter(StructurePiece.class, BoundingBox.class, StructurePiece::getBoundingBox);
            Converters.registerConverter(GeneratedStructure.class, BoundingBox.class, GeneratedStructure::getBoundingBox);
        }



        if (Classes.getExactClassInfo(VoxelShape.class) == null)
            Classes.registerClass(new ClassInfo<>(VoxelShape.class, "voxelshape")
                    .user("voxel ?shapes?")
                    .name("Voxel Shape")
                    .description("A Voxel Shape, usually used for blocks such as Stairs.\n\nStored voxel shapes are not variable persistent, meaning that they shouldn't be stored for use over restarts.") // add example
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
                        public @NotNull String toString(final @NotNull VoxelShape s, final int flags) {
                            return "VoxelShape [" + (s.getBoundingBoxes().stream().map(box -> AddonUtils.withSkriptNumberAccuracy(box.toString())).collect(Collectors.joining(", "))) + "]";
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


        Classes.getExactClassInfo(Block.class).property(
                BOUNDING_BOX,
                "The bounding box that spans across all the parts of a block.",
                addon,
                ExpressionPropertyHandler.of(Block::getBoundingBox, BoundingBox.class)
        );

        Classes.getExactClassInfo(VoxelShape.class).property(
                BOUNDING_BOXES,
                "The bounding boxes of a voxel shape.",
                addon,
                new BoundingBoxesHandler()
        );

        Classes.getExactClassInfo(BoundingBox.class)
                .property(
                        Property.CONTAINS,
                        "Whether one or more voxel shapes, bounding boxes, locations or vectors are fully contained in a bounding box.",
                        addon,
                        new BoundingBoxContainsHandler())
                .property(
                        BaseModule.VOLUME,
                        "The volume of a bounding box.",
                        addon,
                        ExpressionPropertyHandler.of(BoundingBox::getVolume, Double.class)
                );

        Classes.getExactClassInfo(Block.class).property(
                VOXEL_SHAPE,
                "The voxel shape of a block, contains all the parts of its hitbox.",
                addon,
                ExpressionPropertyHandler.of(Block::getCollisionShape, VoxelShape.class)
        );

        Classes.getExactClassInfo(Entity.class).property(
                BOUNDING_BOX,
                "The hitbox of an entity.",
                addon,
                ExpressionPropertyHandler.of(Entity::getBoundingBox, BoundingBox.class)
        );
    }

    @Override
    public String name() {
        return "bounding box";
    }

    public static class BoundingBoxContainsHandler implements ContainsHandler<BoundingBox, Object> {
        @Override
        public boolean contains(BoundingBox container, @NotNull Object element) {
            return switch (element) {
                case VoxelShape voxelShape -> CommonUtils.findFirst(voxelShape.getBoundingBoxes(), box -> !container.contains(box)) == null;
                case BoundingBox box -> container.contains(box);
                case Vector vector -> container.contains(vector);
                case Location location -> container.contains(location.toVector());
                default -> false;
            };
        }

        @Contract(value = " -> new", pure = true)
        @Override
        public Class<?> @NotNull [] elementTypes() {
            return new Class[]{VoxelShape.class, BoundingBox.class, Vector.class, Location.class};
        }
    }

    public static class BoundingBoxesHandler implements ExpressionPropertyHandler<VoxelShape, Object> {

        @Override
        public @Nullable BoundingBox[] convert(VoxelShape propertyHolder) {
            return propertyHolder.getBoundingBoxes().toArray(BoundingBox[]::new);
        }

        @Override
        public @NotNull Class<Object> returnType() {
            return (Class<Object>) (Class) BoundingBox.class;
        }
    }
}
