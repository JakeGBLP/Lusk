package it.jakegblp.lusk.elements.classes;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.destroystokyo.paper.event.entity.EndermanEscapeEvent;
import com.vdurmont.semver4j.Semver;
import it.jakegblp.lusk.classes.ArmorStandInteraction;
import it.jakegblp.lusk.wrappers.EnumWrapper;
import org.bukkit.block.BlockFace;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Pose;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class Types {
    static {
        if (Skript.classExists("org.bukkit.inventory.EquipmentSlot") && Classes.getExactClassInfo(EquipmentSlot.class) == null) {
            EnumWrapper<EquipmentSlot> EQUIPMENTSLOT_ENUM = new EnumWrapper<>(EquipmentSlot.class);
            Classes.registerClass(EQUIPMENTSLOT_ENUM.getClassInfo("equipmentslot")
                    .user("equipment ?slots?")
                    .name("Equipment Slot")
                    .description("All the Equipment Slots.")
                    .examples("best equipment slot for sword is hand")
                    .since("1.0.0"));
        }
        if (Skript.classExists("org.bukkit.block.banner.PatternType") && Classes.getExactClassInfo(PatternType.class) == null) {
            EnumWrapper<PatternType> PATTERNTYPE_ENUM = new EnumWrapper<>(PatternType.class);
            Classes.registerClass(PATTERNTYPE_ENUM.getClassInfo("patterntype")
                    .user("pattern ?types?")
                    .name("Pattern Type")
                    .description("All the Pattern Types.") // add example
                    .since("1.0.0"));
        }
        if (Skript.classExists("com.destroystokyo.paper.event.entity.EndermanEscapeEvent$Reason") && Classes.getExactClassInfo(EndermanEscapeEvent.Reason.class) == null) {
            EnumWrapper<EndermanEscapeEvent.Reason> ENDERMANESCAPEREASON_ENUM = new EnumWrapper<>(EndermanEscapeEvent.Reason.class);
            Classes.registerClass(ENDERMANESCAPEREASON_ENUM.getClassInfo("endermanescapereason")
                    .user("enderman ?escape ?reasons?")
                    .name("Enderman Escape Reason")
                    .description("All the Valid Enderman Escape Reasons.") // add example
                    .since("1.0.0"));
        }
        if (Classes.getExactClassInfo(Semver.class) == null)
            Classes.registerClass(new ClassInfo<>(Semver.class, "version")
                    .user("versions?")
                    .name("Version")
                    .description("A Minecraft Version.")
                    .usage("")
                    .examples("") // add example
                    .since("1.0.0")
                    .parser(new Parser<>() {
                        @Override
                        public @NotNull Semver parse(final @NotNull String s, final @NotNull ParseContext context) {
                            return new Semver(s, Semver.SemverType.LOOSE);
                        }

                        @Override
                        public boolean canParse(final @NotNull ParseContext context) {
                            return false;
                        }

                        @Override
                        public @NotNull String toString(final Semver v, final int flags) {
                            return v.toString();
                        }

                        @Override
                        public @NotNull String toVariableNameString(final Semver v) {
                            return v.toString();
                        }

                        @Override
                        public @NotNull String getDebugMessage(final Semver v) {
                            return toString(v, 0) + " version (" + v + ")";
                        }
                    }));
        if (Skript.classExists("org.bukkit.entity.Pose") && Classes.getExactClassInfo(Pose.class) == null) {
            EnumWrapper<Pose> POSE_ENUM = new EnumWrapper<>(Pose.class);
            Classes.registerClass(POSE_ENUM.getClassInfo("pose")
                    .user("poses?")
                    .name("Pose")
                    .description("All the Poses.") // add example
                    .since("1.0.2"));
        }
        if (Skript.classExists("org.bukkit.entity.SpawnCategory") && Classes.getExactClassInfo(SpawnCategory.class) == null) {
            EnumWrapper<SpawnCategory> SPAWNCATEGORY_ENUM = new EnumWrapper<>(SpawnCategory.class);
            Classes.registerClass(SPAWNCATEGORY_ENUM.getClassInfo("spawncategory")
                    .user("spawn ?categor(y|ies)")
                    .name("Spawn Category")
                    .description("All the Spawn Categories.") // add example
                    .since("1.0.2"));
        }
        if (Classes.getExactClassInfo(BoundingBox.class) == null)
            Classes.registerClass(new ClassInfo<>(BoundingBox.class, "boundingbox")
                    .user("bounding ?box(es)?")
                    .name("Bounding Box")
                    .description("A Bounding Box")
                    .usage("")// add example
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
        if (Skript.classExists("org.bukkit.event.block.CauldronLevelChangeEvent$ChangeReason") && Classes.getExactClassInfo(CauldronLevelChangeEvent.ChangeReason.class) == null) {
            EnumWrapper<CauldronLevelChangeEvent.ChangeReason> CAULDRONCHANGEREASON_ENUM = new EnumWrapper<>(CauldronLevelChangeEvent.ChangeReason.class);
            Classes.registerClass(CAULDRONCHANGEREASON_ENUM.getClassInfo("cauldronchangereason")
                    .user("cauldron ?change ?reasons?")
                    .name("Cauldron Change Reason")
                    .description("All the Cauldron Change Reasons.") // add example
                    .since("1.0.2"));
        }
        if (Skript.classExists("org.bukkit.entity.EnderDragon$Phase") && Classes.getExactClassInfo(EnderDragon.Phase.class) == null) {
            EnumWrapper<EnderDragon.Phase> ENDERDRAGONPHASE_ENUM = new EnumWrapper<>(EnderDragon.Phase.class);
            Classes.registerClass(ENDERDRAGONPHASE_ENUM.getClassInfo("enderdragonphase")
                     .user("ender ?dragon ? phases?")
                     .name("Ender Dragon Phase")
                     .description("All the Ender Dragon Phases.") // add example
                     .since("1.0.2"));
        }
        if (Skript.classExists("org.bukkit.block.BlockFace") && Classes.getExactClassInfo(BlockFace.class) == null) {
            EnumWrapper<BlockFace> BLOCKFACE_ENUM = new EnumWrapper<>(BlockFace.class);
            Classes.registerClass(BLOCKFACE_ENUM.getClassInfo("blockface")
                    .user("block ?faces?")
                    .name("Block Face")
                    .description("All the Block Faces.") // add example
                    .since("1.1"));
        }
        EnumWrapper<ArmorStandInteraction> ARMORSTANDINTERACTION_ENUM = new EnumWrapper<>(ArmorStandInteraction.class);
        Classes.registerClass(ARMORSTANDINTERACTION_ENUM.getClassInfo("armorstandinteraction")
                .user("armor( |-)?stand interactions?")
                .name("Armor Stand Interaction")
                .description("All the Armor Stand Interactions.") // add example
                .since("1.1.1"));
    }
}
