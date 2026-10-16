package it.jakegblp.lusk.skript.modules.base;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Color;
import ch.njol.skript.util.SkriptColor;
import ch.njol.skript.util.Version;
import ch.njol.yggdrasil.Fields;
import io.papermc.paper.math.Rotations;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.common.Instances;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import it.jakegblp.lusk.nms.core.world.entity.attribute.MutableAttributeModifier;
import it.jakegblp.lusk.skript.api.changer.SimplePluralChanger;
import it.jakegblp.lusk.skript.api.classinfo.EnumClassInfoWrapper;
import it.jakegblp.lusk.skript.api.classinfo.EnumLikeClassInfoWrapper;
import it.jakegblp.lusk.skript.api.classinfo.SimpleClassInfo;
import it.jakegblp.lusk.skript.api.module.LuskModule;
import it.jakegblp.lusk.skript.api.parser.SimpleParser;
import it.jakegblp.lusk.skript.modules.base.conditions.*;
import it.jakegblp.lusk.skript.modules.base.effects.*;
import it.jakegblp.lusk.skript.modules.base.events.*;
import it.jakegblp.lusk.skript.modules.base.expressions.*;
import it.jakegblp.lusk.skript.modules.base.sections.SecClientSidedHealthHungerSaturation;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import it.jakegblp.lusk.skript.utils.BlockDataHolder;
import it.jakegblp.lusk.skript.utils.BlockStateHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.DyeColor;
import org.bukkit.EntityEffect;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Armadillo;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.lang.converter.Converter;
import org.skriptlang.skript.lang.converter.Converters;
import org.skriptlang.skript.lang.properties.Property;
import org.skriptlang.skript.lang.properties.handlers.WXYZHandler;
import org.skriptlang.skript.lang.properties.handlers.base.ExpressionPropertyHandler;
import org.skriptlang.skript.lang.properties.handlers.base.PropertyHandler;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.io.StreamCorruptedException;
import java.util.function.IntConsumer;

public class BaseModule extends LuskModule {

    public static final Property<ExpressionPropertyHandler<?, ?>> VOLUME = Property.of(
            "volume",
            "The volume of an object.",
            "2.0.0",
            Instances.SKRIPT_ADDON,
            ExpressionPropertyHandler.class
    );

    public BaseModule(IntConsumer adder) {
        super(adder);
    }

    @Override
    protected void registration(SyntaxRegistry registry) {
        CondBlockStateIsPlaced.register(registry);
        CondCanBeWaterLogged.register(registry);
        CondCartesian.register(registry);
        CondFullBlock.register(registry);
        CondIsWaxed.register(registry);
        CondJukeboxPlaying.register(registry);
        CondRiseComposterLevel.register(registry);
        CondShouldPlayEmptyEffect.register(registry);
        CondWaterLogged.register(registry);

        EffApplyAttribute.register(registry);
        EffBlockWaterLog.register(registry);
        EffCancelComposterRising.register(registry);
        EffCancelPlayEmptyEffect.register(registry);
        EffClientSidedHealthHungerSaturation.register(registry);
        EffJukeboxEject.register(registry);
        EffJukeboxPlay.register(registry);
        EffWax.register(registry);

        EvtBlockIgnite.register(registry);
        EvtHopperInventorySearch.register(registry);
        EvtLecternPageChange.register(registry);
        EvtLoomPatternSelect.register(registry);
        SimpleEvents.register(registry);

        ExprAttributeModifier.register(registry);
        ExprAttributeSnapshot.register(registry);
        ExprBlockBlastResistance.register(registry);
        ExprBlockFaceBetween.register(registry);
        ExprBlockFaceModXYZ.register(registry);
        ExprBlockFaceOpposite.register(registry);
        ExprBlockLevel.register(registry);
        ExprBlockSlipperiness.register(registry);
        ExprEntityId.register(registry);
        ExprHopperSearchedBlock.register(registry);
        ExprJukeboxRecord.register(registry);
        ExprNamespacedKeyFrom.register(registry);
        ExprNamespacedKeyOf.register(registry);
        ExprProtocolEntityReference.register(registry);
        ExprSecEnchantedBook.register(registry);
        ExprSecList.register(registry);
        ExprSignContent.register(registry);
        ExprSpawnerEntityType.register(registry);
        PropExprVolume.register(registry);

        SecClientSidedHealthHungerSaturation.register(registry);
    }

    @Override
    public void init(SkriptAddon addon) {
        if (Classes.getExactClassInfo(EquipmentSlotGroup.class) == null) {
            EnumLikeClassInfoWrapper<EquipmentSlotGroup> EQUIPMENTSLOT_ENUM = new EnumLikeClassInfoWrapper<>(EquipmentSlotGroup.class, CommonUtils.getEquipmentSlotGroups(), null, "slot group");
            Classes.registerClass(EQUIPMENTSLOT_ENUM.getClassInfo("equipmentslotgroup")
                    .user("equipment ?slot ?groups?")
                    .name("Equipment Slot Group")
                    .description("All the Equipment Slot Groups.\n\nEquipment Slot Groups represent groups of equipment slots.")
                    .since("2.0.0"));
            increaseClassInfoCount(1);
        }
        Classes.registerClass(new SimpleClassInfo<>(ProtocolEntityReference.class, "protocolentityreference")
                .toString(entityReference -> "Protocol Entity with id " + entityReference.getId())
                .defaultExpression(new EventValueExpression<>(ProtocolEntityReference.class))
                .user("protocol ?entity ?references?")
                .name("Protocol Entity - Reference")
                .description("A reference to an entity's id.") // add example
                .since("2.0.0"));
        // Bukkit
        if (Classes.getExactClassInfo(NamespacedKey.class) == null) {
            Classes.registerClass(new ClassInfo<>(NamespacedKey.class, "namespacedkey")
                    .user("namespaced ?keys?")
                    .name("NamespacedKey")
                    .description("A key with a namespace, used to identify objects in minecraft.")
                    .since("2.0.0")
                    .parser(SimpleParser.simple())
                    .serializer(new Serializer<>() {
                        @Override
                        public @NotNull Fields serialize(NamespacedKey namespacedKey) {
                            Fields fields = new Fields();
                            fields.putObject("key", namespacedKey.toString());
                            return fields;
                        }

                        @Override
                        protected NamespacedKey deserialize(Fields fields) throws StreamCorruptedException {
                            String key = fields.getObject("key", String.class);
                            if (key == null) throw new StreamCorruptedException("NamespacedKey string is null");
                            return NamespacedKey.fromString(key);
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
            increaseClassInfoCount(1);
        }
        if (Classes.getExactClassInfo(AttributeModifier.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(AttributeModifier.class, "attributemodifier")
                    .toString(attributeModifier -> "attribute modifier '" + Classes.toString(attributeModifier.getKey())
                            + "' to " + switch (attributeModifier.getOperation()) {
                        case ADD_NUMBER -> "increase the base value";
                        case ADD_SCALAR -> "multiply the base value";
                        case MULTIPLY_SCALAR_1 -> "multiply the total value";
                    } + " by " + attributeModifier.getAmount())
                    .user("attribute ?modifiers?")
                    .name("Attribute - Modifier")
                    .description("Modifies the base value of an attribute by using certain operations. The resulting value after modification is capped by the attribute's minimum and maximum limits. \n\nModifiers have a namespaced identifiers to uniquely identify them."));
            increaseClassInfoCount(1);
        }

        Classes.registerClass(new SimpleClassInfo<>(MutableAttributeModifier.class, "mutableattributemodifier")
                .toString(mutableAttributeModifier -> (mutableAttributeModifier.isTransient() ? "" : "non-") + "transient attribute modifier '" + Classes.toString(mutableAttributeModifier.getKey())
                        + "' to " + switch (mutableAttributeModifier.getOperation()) {
                    case ADD_NUMBER -> "increase the base value";
                    case ADD_SCALAR -> "multiply the base value";
                    case MULTIPLY_SCALAR_1 -> "multiply the total value";
                } + " by " + mutableAttributeModifier.getAmount())
                .user("mutable ?attribute ?modifiers?")
                .name("Attribute - Mutable Modifier")
                .description("Modifies the base value of an attribute by using certain operations.\nThe resulting value after modification is capped by the attribute's minimum and maximum limits. \n\nModifiers have a namespaced identifiers to uniquely identify them.\n\n*This type includes the `isTransient` property."));
        Classes.registerClass(new SimpleClassInfo<>(AttributeSnapshot.class, "attributesnapshot")
                .toString(attributeSnapshot -> Classes.toString(attributeSnapshot.getAttribute())
                        + " Attribute with base " + attributeSnapshot.getBase()
                        + " and modifiers " + Classes.toString(attributeSnapshot.getModifiers()))
                .user("attribute ?modifiers?")
                .name("Attribute - Modifier")
                .changer(SimplePluralChanger.simpleListChanger(AttributeSnapshot.class, AttributeModifier.class, AttributeModifier[].class))
                .description("Modifies the base value of an attribute by using certain operations. The resulting value after modification is capped by the attribute's minimum and maximum limits. \n\nModifiers have a namespaced identifiers to uniquely identify them."));

        /*
        Mutability and cross-addon support: if another addon registers the immutable object as a classinfo,
         instances of the mutable one can be converted to that type.
         */
        Converters.registerConverter(MutableAttributeModifier.class, AttributeModifier.class, MutableAttributeModifier::immutable);
        Converters.registerConverter(AttributeModifier.class, MutableAttributeModifier.class, MutableAttributeModifier::new);
        if (Classes.getExactClassInfo(Keyed.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(Keyed.class, "keyed")
                    .toString(keyed -> "Object with key " + keyed.getKey())
                    .user("keyeds?")
                    .name("Keyed")
                    .description("An object with a namespaced key attached to it.") // add example
                    .since("2.0.0"));
            increaseClassInfoCount(1);
        }
        if (Classes.getExactClassInfo(Pose.class) == null) {
            EnumClassInfoWrapper<Pose> POSE_ENUM = new EnumClassInfoWrapper<>(Pose.class);
            Classes.registerClass(POSE_ENUM.getClassInfo("pose")
                    .user("poses?")
                    .name("Entity - Pose")
                    .description("All the Poses.") // add example
                    .since("1.0.2, 1.3.3 (Suffix)"));
            increaseClassInfoCount(1);
        }
        if (Classes.getExactClassInfo(BlockState.class) == null) {
            Classes.registerClass(new ClassInfo<>(BlockState.class, "blockstate")
                    .user("block ?states?")
                    .name("Block State")
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
                            return (blockState.isPlaced() ? "placed " : "") + "block state of type '" + blockState.getType() + "' at '" + blockState.getLocation() + '"';
                        }

                        @Override
                        public @NotNull String toVariableNameString(BlockState blockState) {
                            return toString(blockState, 0);
                        }
                    }));
            increaseClassInfoCount(1);
        }
        if (Skript.getVersion().isSmallerThan(new Version(2, 14)) && Classes.getExactClassInfo(EntityEffect.class) == null) {
            EnumClassInfoWrapper<EntityEffect> ENTITY_EFFECT_ENUM = new EnumClassInfoWrapper<>(EntityEffect.class);
            Classes.registerClass(ENTITY_EFFECT_ENUM.getClassInfo("entityeffect")
                    .user("entit(y|ies) ?effects?")
                    .name("Entity Effect")
                    .description("Effects that can be played on entities.\nAuto-generated\n\nDisabled when running Skript 2.14+") // add example
                    .since("2.0.0"));
            increaseClassInfoCount(1);
        }

        // Kyori
        if (Classes.getExactClassInfo(Component.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(Component.class, "textcomponent")
                    .toString(component -> MiniMessage.miniMessage().serialize(component))
                    .user("text ?components?")
                    .name("Text Component")
                    .description("An object representing text with formatting like colors, decorations and events.") // add example
                    .since("2.0.0"));
            increaseClassInfoCount(1);
        }
        if (Classes.getExactClassInfo(ComponentBuilder.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(ComponentBuilder.class, "mutabletextcomponent")
                    .toString(component -> MiniMessage.miniMessage().serialize(component.build()))
                    .user("mutable ?text ?components?")
                    .name("Mutable Text Component")
                    .description("Same as a Text Component, but can be modified.") // add example
                    .since("2.0.0"));
            increaseClassInfoCount(1);
        }

        if (Classes.getExactClassInfo(EulerAngle.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(EulerAngle.class, "eulerangle")
                    .toString(eulerAngle -> AddonUtils.withSkriptNumberAccuracy(eulerAngle.toString()))
                    .user("euler ?angles?")
                    .name("Euler Angle")
                    .description("EulerAngle is used to represent 3 angles, one for each axis (x, y, z). The angles are in radians") // add example
                    .since("1.3")
                    .serializer(new Serializer<>() {
                        @Override
                        public @NotNull Fields serialize(EulerAngle o) {
                            Fields f = new Fields();
                            f.putPrimitive("x", o.getX());
                            f.putPrimitive("y", o.getY());
                            f.putPrimitive("z", o.getZ());
                            return f;
                        }

                        @Override
                        public void deserialize(EulerAngle o, @NotNull Fields f) {
                        }

                        @Override
                        protected EulerAngle deserialize(@NotNull Fields f) throws StreamCorruptedException {
                            return new EulerAngle(
                                    f.getPrimitive("x", double.class),
                                    f.getPrimitive("y", double.class),
                                    f.getPrimitive("z", double.class)
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
                    })
                    .property(Property.WXYZ, "The X, Y, or Z component of the euler angle.", addon, new EulerAngleWXYZHandler()));
            increaseClassInfoCount(1);
        }
        if (Classes.getExactClassInfo(BlockFace.class) == null) {
            EnumClassInfoWrapper<BlockFace> BLOCKFACE_ENUM = new EnumClassInfoWrapper<>(BlockFace.class, null, "face");
            Classes.registerClass(BLOCKFACE_ENUM.getClassInfo("blockface")
                    .user("(block ?)?faces?")
                    .name("Block Face")
                    .description("All the Block Faces.")
                    .examples("south face", "up face")
                    .since("1.1"));
            increaseClassInfoCount(1);
        }

        if (Classes.getExactClassInfo(Action.class) == null) {
            EnumClassInfoWrapper<Action> BLOCK_ACTION_ENUM = new EnumClassInfoWrapper<>(Action.class);
            Classes.registerClass(BLOCK_ACTION_ENUM.getClassInfo("blockaction")
                    .user("block ?actions?")
                    .name("Block Action")
                    .description("All the Block Actions.")
                    .examples("physical", "left click air")
                    .since("1.3"));
            increaseClassInfoCount(1);
        }
        if (Classes.getExactClassInfo(BlockIgniteEvent.IgniteCause.class) == null) {
            var IGNITE_CAUSE_ENUM = new EnumClassInfoWrapper<>(BlockIgniteEvent.IgniteCause.class, null, "ignition");
            Classes.registerClass(IGNITE_CAUSE_ENUM.getClassInfo("ignitioncause")
                    .user("ignit(e|ion) ?causes?")
                    .name("Ignition Cause")
                    .description("All the Ignition Causes.")
                    .since("1.3"));
            increaseClassInfoCount(1);
        }

        if (Classes.getExactClassInfo(CauldronLevelChangeEvent.ChangeReason.class) == null) {
            var CAULDRON_CHANGE_REASON_ENUM = new EnumClassInfoWrapper<>(CauldronLevelChangeEvent.ChangeReason.class);
            Classes.registerClass(CAULDRON_CHANGE_REASON_ENUM.getClassInfo("cauldronchangereason")
                    .user("cauldron ?change ?reasons?")
                    .name("Cauldron Change Reason")
                    .examples("armor_wash")
                    .description("All the Cauldron Change Reasons.")
                    .since("1.0.2"));
            increaseClassInfoCount(1);
        }

        Classes.registerClass(new SimpleClassInfo<>(BlockDataHolder.class, "blockdataholder")
                .toString(holder -> switch (holder) {
                    case BlockDataHolder.Block _ -> "block";
                    case BlockDataHolder.BlockState _ -> "block state";
                    case BlockDataHolder.Item _ -> "item";
                    default -> throw new IllegalStateException("Invalid block data holder: " + holder.getClass().getSimpleName());
                } + " holding block data")
                .user("block ?data? holders?")
                .name("Block Data Holder")
                .description("An object which holds a blockdata object and can have it be updated.")
                .after("blockdata")
                .since("2.0.0"));

        Classes.registerClass(new SimpleClassInfo<>(BlockStateHolder.class, "blockstateholder")
                .toString(holder -> switch (holder) {
                    case BlockStateHolder.Block _ -> "block";
                    case BlockStateHolder.Item _ -> "item";
                    default -> throw new IllegalStateException("Invalid block state holder: " + holder.getClass().getSimpleName());
                } + " holding block state")
                .user("block ?state? holders?")
                .name("Block State Holder")
                .description("An object which holds a blockstate object and can have it be updated.")
                .after("blockstate")
                .since("2.0.0"));

        if (Classes.getExactClassInfo(Armadillo.State.class) == null) {
            EnumClassInfoWrapper<Armadillo.State> ARMOR_STAND_INTERACTION_ENUM = new EnumClassInfoWrapper<>(Armadillo.State.class);
            Classes.registerClass(ARMOR_STAND_INTERACTION_ENUM.getClassInfo("armadillostate")
                    .user("armadillo ?states?")
                    .name("Armadillo State")
                    .description("All the Armadillo States.") // add example
                    .since("1.3.8"));
            increaseClassInfoCount(1);
        }

        if (Classes.getExactClassInfo(Rotations.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(Rotations.class, "rotation")
                    .toString(rotation -> "rotation with x: " + rotation.x() + " y: " + rotation.y() + " z: " + rotation.z())
                    .user("rotations?")
                    .name("Rotation")
                    .description("An immutable object that stores rotations in degrees for all axis.")
                    .since("2.0.0")
                    .serializer(new Serializer<>() {
                        @Override
                        public @NotNull Fields serialize(Rotations o) {
                            Fields f = new Fields();
                            f.putPrimitive("x", o.x());
                            f.putPrimitive("y", o.y());
                            f.putPrimitive("z", o.z());
                            return f;
                        }

                        @Override
                        public void deserialize(Rotations o, @NotNull Fields f) {
                        }

                        @Override
                        protected Rotations deserialize(@NotNull Fields f) throws StreamCorruptedException {
                            return Rotations.ofDegrees(
                                    f.getPrimitive("x", double.class),
                                    f.getPrimitive("y", double.class),
                                    f.getPrimitive("z", double.class)
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
                    })
                    .property(Property.WXYZ, "The X, Y, or Z component of the euler angle.", addon, new RotationsWXYZHandler()));
            increaseClassInfoCount(1);
        }
        if (!Converters.exactConverterExists(EulerAngle.class, Rotations.class))
            Converters.registerConverter(EulerAngle.class, Rotations.class, eulerAngle -> Rotations.ofDegrees(Math.toDegrees(eulerAngle.getX()), Math.toDegrees(eulerAngle.getY()), Math.toDegrees(eulerAngle.getZ())));
        if (!Converters.exactConverterExists(Rotations.class, EulerAngle.class))
            Converters.registerConverter(Rotations.class, EulerAngle.class, rotations -> new EulerAngle(Math.toRadians(rotations.x()), Math.toRadians(rotations.y()), Math.toRadians(rotations.z())));
        if (!Converters.exactConverterExists(Rotations.class, Vector.class))
            Converters.registerConverter(BlockFace.class, Vector.class, BlockFace::getDirection);
        if (!Converters.exactConverterExists(BlockFace.class, Vector.class))
            Converters.registerConverter(BlockFace.class, Vector.class, BlockFace::getDirection);
        if (!Converters.exactConverterExists(Block.class, BlockData.class))
            Converters.registerConverter(Block.class, BlockData.class, Block::getBlockData);
        if (!Converters.exactConverterExists(BlockState.class, BlockData.class))
            Converters.registerConverter(BlockState.class, BlockData.class, BlockState::getBlockData);
        if (!Converters.exactConverterExists(BlockState.class, Block.class))
            Converters.registerConverter(BlockState.class, Block.class, BlockState::getBlock);
        if (!Converters.exactConverterExists(Block.class, BlockState.class))
            Converters.registerConverter(Block.class, BlockState.class, Block::getState);
        if (!Converters.exactConverterExists(ItemStack.class, BlockData.class))
            Converters.registerConverter(ItemStack.class, BlockData.class, item -> item.getItemMeta() instanceof BlockDataMeta meta ? meta.getBlockData(item.getType()) : null, Converter.NO_RIGHT_CHAINING);
        if (!Converters.exactConverterExists(ItemStack.class, BlockState.class))
            Converters.registerConverter(ItemStack.class, BlockState.class, item -> item.getItemMeta() instanceof BlockStateMeta meta ? meta.getBlockState() : null, Converter.NO_RIGHT_CHAINING);
        if (!Converters.exactConverterExists(ItemStack.class, BlockState.class))
            Converters.registerConverter(ItemStack.class, Block.class, item -> item.getItemMeta() instanceof BlockStateMeta meta ? meta.getBlockState().getBlock() : null, Converter.NO_RIGHT_CHAINING);
        Converters.registerConverter(Block.class, BlockDataHolder.class, BlockDataHolder::block, Converter.NO_RIGHT_CHAINING);
        Converters.registerConverter(BlockState.class, BlockDataHolder.class, BlockDataHolder::blockState, Converter.NO_RIGHT_CHAINING);
        Converters.registerConverter(ItemType.class, BlockDataHolder.class, BlockDataHolder::item, Converter.NO_RIGHT_CHAINING);
        Converters.registerConverter(Block.class, BlockStateHolder.class, BlockStateHolder::block, Converter.NO_RIGHT_CHAINING);
        Converters.registerConverter(ItemType.class, BlockStateHolder.class, BlockStateHolder::item, Converter.NO_RIGHT_CHAINING);
        Converters.registerConverter(EquipmentSlot.class, EquipmentSlotGroup.class, EquipmentSlot::getGroup);
        Converters.registerConverter(Integer.class, ProtocolEntityReference.class, ProtocolEntityReference::of);
        Converters.registerConverter(Entity.class, ProtocolEntityReference.class, ProtocolEntityReference::of);
        Converters.registerConverter(ProtocolEntityReference.class, Integer.class, ProtocolEntityReference::getId);
        //Converters.registerConverter(ComponentBuilder.class, Component.class, ComponentBuilder::build);
        Converters.registerConverter(Color.class, DyeColor.class, Color::asDyeColor);
        Converters.registerConverter(DyeColor.class, Color.class, SkriptColor::fromDyeColor);

        increaseClassInfoCount(4);
    }

    @Override
    public String name() {
        return "base";
    }

    private static class EulerAngleWXYZHandler extends WXYZHandler<EulerAngle, Double> {

        @Override
        public PropertyHandler<EulerAngle> newInstance() {
            var instance = new EulerAngleWXYZHandler();
            instance.axis(axis);
            return instance;
        }

        @Override
        public @Nullable Double convert(EulerAngle propertyHolder) {
            return switch (axis) {
                case X -> propertyHolder.getX();
                case Y -> propertyHolder.getY();
                case Z -> propertyHolder.getZ();
                default -> null;
            };
        }

        @Override
        public boolean supportsAxis(Axis axis) {
            return axis != Axis.W;
        }

        @Override
        public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
            return switch (mode) {
                case ADD, SET, REMOVE -> new Class[]{Double.class};
                default -> null;
            };
        }

        @Override
        public void change(EulerAngle eulerAngle, Object @Nullable [] delta, Changer.ChangeMode mode) {
            assert delta != null;
            float value = ((Float) delta[0]);
            if (axis == Axis.W)
                return;
            switch (mode) {
                case REMOVE:
                    value = -value;
                case ADD:
                    switch (axis) {
                        case X -> eulerAngle.setX(eulerAngle.getX() + value);
                        case Y -> eulerAngle.setY(eulerAngle.getY() + value);
                        case Z -> eulerAngle.setZ(eulerAngle.getZ() + value);
                    }
                    break;
                case SET:
                    switch (axis) {
                        case X -> eulerAngle.setX(value);
                        case Y -> eulerAngle.setY(value);
                        case Z -> eulerAngle.setZ(value);
                    }
                    break;
                default:
                    assert false;
            }
        }

        @Override
        public @NotNull Class<Double> returnType() {
            return Double.class;
        }

        @Override
        public boolean requiresSourceExprChange() {
            return true;
        }
    }

    private static class RotationsWXYZHandler extends WXYZHandler<Rotations, Double> {

        @Override
        public PropertyHandler<Rotations> newInstance() {
            var instance = new RotationsWXYZHandler();
            instance.axis(axis);
            return instance;
        }

        @Override
        public @Nullable Double convert(Rotations propertyHolder) {
            return switch (axis) {
                case X -> propertyHolder.x();
                case Y -> propertyHolder.y();
                case Z -> propertyHolder.z();
                default -> null;
            };
        }

        @Override
        public boolean supportsAxis(Axis axis) {
            return axis != Axis.W;
        }

        @Override
        public @NotNull Class<Double> returnType() {
            return Double.class;
        }

        @Override
        public boolean requiresSourceExprChange() {
            return true;
        }
    }
}
