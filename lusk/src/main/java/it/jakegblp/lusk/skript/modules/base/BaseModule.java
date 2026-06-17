package it.jakegblp.lusk.skript.modules.base;

import ch.njol.skript.Skript;
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
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import it.jakegblp.lusk.common.CommonUtils;
import it.jakegblp.lusk.common.URIUtils;
import it.jakegblp.lusk.nms.core.world.entity.ProtocolEntityReference;
import it.jakegblp.lusk.nms.core.world.entity.attribute.AttributeSnapshot;
import it.jakegblp.lusk.nms.core.world.entity.attribute.MutableAttributeModifier;
import it.jakegblp.lusk.nms.core.world.player.profile.CompletablePlayerProfile;
import it.jakegblp.lusk.nms.core.world.player.profile.TexturesPayload;
import it.jakegblp.lusk.skript.api.changer.SimplePluralChanger;
import it.jakegblp.lusk.skript.api.classinfo.EnumClassInfoWrapper;
import it.jakegblp.lusk.skript.api.classinfo.EnumLikeClassInfoWrapper;
import it.jakegblp.lusk.skript.api.classinfo.SimpleClassInfo;
import it.jakegblp.lusk.skript.api.module.LuskModule;
import it.jakegblp.lusk.skript.api.parser.SimpleParser;
import it.jakegblp.lusk.skript.modules.base.effects.EffApplyAttribute;
import it.jakegblp.lusk.skript.modules.base.expressions.*;
import it.jakegblp.lusk.skript.modules.base.sections.SecClientSidedHealthHungerSaturation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.DyeColor;
import org.bukkit.EntityEffect;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pose;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.lang.converter.Converters;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.io.StreamCorruptedException;
import java.net.URI;

public class BaseModule extends LuskModule {

    @Override
    protected void registration(SyntaxRegistry registry) {
        ExprSecList.register(registry);
        ExprEntityId.register(registry);
        ExprNamespacedKeyFrom.register(registry);
        SecClientSidedHealthHungerSaturation.register(registry);
        ExprAttributeSnapshot.register(registry);
        ExprAttributeModifier.register(registry);
        EffApplyAttribute.register(registry);
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
        }
        Classes.registerClass(new SimpleClassInfo<>(TexturesPayload.class, "texturepayload")
                .toString(texturesPayload -> (texturesPayload.getSignature() != null ? "Signed " : "") + "Texture Payload with name " + texturesPayload.getProfileName())
                .user("texture ?payloads?")
                .name("Player - Texture Payload")
                .description("Contains all the texture data of a player.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(ProtocolEntityReference.class, "protocolentityreference")
                .toString(entityReference -> "Protocol Entity with id " + entityReference.getId())
                .defaultExpression(new EventValueExpression<>(ProtocolEntityReference.class))
                .user("protocol ?entity ?references?")
                .name("Protocol Entity - Reference")
                .description("A reference to an entity's id.") // add example
                .since("2.0.0"));
        Classes.registerClass(new SimpleClassInfo<>(CompletablePlayerProfile.class, "completableplayerprofile")
                .toString(playerProfile -> "Completable Player Profile with name " + playerProfile.getName() + " and UUID " + playerProfile.getId())
                .user("completable ?(player ?)?profiles?")
                .name("Player - Completable Profile")
                .description("A player's profile that's completable.\n\nContains the player's UUID, name, skin, cape, player model (alex/steve) and profile properties.") // add example
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
        }
        if (Skript.classExists("org.bukkit.entity.Pose") && Classes.getExactClassInfo(Pose.class) == null) {
            EnumClassInfoWrapper<Pose> POSE_ENUM = new EnumClassInfoWrapper<>(Pose.class);
            Classes.registerClass(POSE_ENUM.getClassInfo("pose")
                    .user("poses?")
                    .name("Entity - Pose")
                    .description("All the Poses.") // add example
                    .since("1.0.2, 1.3.3 (Suffix)"));
        }
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
                            return (blockState.isPlaced() ? "placed " : "") + "BlockState of type '" + blockState.getType() + "' at '" + blockState.getLocation() + '"';
                        }

                        @Override
                        public @NotNull String toVariableNameString(BlockState blockState) {
                            return toString(blockState, 0);
                        }
                    }));
        }
        if (Skript.getVersion().isSmallerThan(new Version(2,14)) && Classes.getExactClassInfo(EntityEffect.class) == null) {
            EnumClassInfoWrapper<EntityEffect> ENTITY_EFFECT_ENUM = new EnumClassInfoWrapper<>(EntityEffect.class);
            Classes.registerClass(ENTITY_EFFECT_ENUM.getClassInfo("entityeffect")
                    .user("entit(y|ies) ?effects?")
                    .name("Entity Effect")
                    .description("Effects that can be played on entities.\nAuto-generated\n\nDisabled when running Skript 2.14+") // add example
                    .since("2.0.0"));
        }

        // Paper
        if (Classes.getExactClassInfo(PlayerProfile.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(PlayerProfile.class, "playerprofile")
                    .toString(playerProfile -> "Player Profile with name " + playerProfile.getName() + " and UUID " + playerProfile.getId())
                    .user("(player ?)?profiles?")
                    .name("Player - Profile")
                    .description("A player's profile.\n\nContains the player's UUID, name, skin, cape, player model (alex/steve) and profile properties.") // add example
                    .since("2.0.0"));
        }
        if (Classes.getExactClassInfo(ProfileProperty.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(ProfileProperty.class, "playerprofileproperty")
                    .toString(profileProperty -> "Player Profile Property named " + profileProperty.getName() + " with value " + profileProperty.getValue() + (profileProperty.isSigned() ? " and signature " + profileProperty.getSignature() : ""))
                    .user("(player ?)?profile ?propert(y|ies)")
                    .name("Player Profile - Property")
                    .description("A property of a player profile.\nUsed for skin and cape textures.") // add example
                    .since("2.0.0"));
        }

        // Kyori
        if (Classes.getExactClassInfo(Component.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(Component.class, "textcomponent")
                    .toString(component -> MiniMessage.miniMessage().serialize(component))
                    .user("text ?components?")
                    .name("Text Component")
                    .description("An object representing text with formatting like colors, decorations and events.") // add example
                    .since("2.0.0"));
        }
        if (Classes.getExactClassInfo(ComponentBuilder.class) == null) {
            Classes.registerClass(new SimpleClassInfo<>(ComponentBuilder.class, "mutabletextcomponent")
                    .toString(component -> MiniMessage.miniMessage().serialize(component.build()))
                    .user("mutable ?text ?components?")
                    .name("Mutable Text Component")
                    .description("Same as a Text Component, but can be modified.") // add example
                    .since("2.0.0"));
        }

        // Other
        if (Classes.getExactClassInfo(URI.class) == null) {
            Classes.registerClass(new ClassInfo<>(URI.class, "path")
                    .user("(path|url)s?")
                    .name("Resource Path/URL")
                    .description("Represents a path to something, such as a relative file path or an internet URL.\n\nCopied from skript-io, skript-io's will always have priority.")
                    .since("2.0.0"));
        }

        Converters.registerConverter(EquipmentSlot.class, EquipmentSlotGroup.class, EquipmentSlot::getGroup);
        Converters.registerConverter(Integer.class, ProtocolEntityReference.class, ProtocolEntityReference::of);
        Converters.registerConverter(Entity.class, ProtocolEntityReference.class, ProtocolEntityReference::of);
        Converters.registerConverter(ProtocolEntityReference.class, Integer.class, ProtocolEntityReference::getId);
        Converters.registerConverter(ComponentBuilder.class, Component.class, ComponentBuilder::build);
        Converters.registerConverter(PlayerProfile.class, CompletablePlayerProfile.class, CompletablePlayerProfile::new);
        Converters.registerConverter(CompletablePlayerProfile.class, PlayerProfile.class, CompletablePlayerProfile::getPlayerProfile);
        if (!Converters.exactConverterExists(String.class, URI.class))
            Converters.registerConverter(String.class, URI.class, URIUtils::toURI);
        if (!Converters.exactConverterExists(String.class, Component.class))
            Converters.registerConverter(String.class, Component.class, string -> string.contains("§") ? Component.text(string) : MiniMessage.miniMessage().deserialize(string));
        Converters.registerConverter(Color.class, DyeColor.class, Color::asDyeColor);
        Converters.registerConverter(DyeColor.class, Color.class, SkriptColor::fromDyeColor);
    }

    @Override
    public String name() {
        return "base";
    }
}
