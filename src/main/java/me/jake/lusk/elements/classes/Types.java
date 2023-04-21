package me.jake.lusk.elements.classes;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumSerializer;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.destroystokyo.paper.event.entity.EndermanEscapeEvent.Reason;
import com.vdurmont.semver4j.Semver;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Pose;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.event.block.CauldronLevelChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;

public class Types {
    static {
        Classes.registerClass(new ClassInfo<>(EquipmentSlot.class, "equipmentslot")
                .user("equipment ?slots?")
                .name("Equipment Slot")
                .description("All the Equipment Slots of a player.")
                .usage(Arrays.toString(EquipmentSlot.values()))
                .examples("best equipment slot for sword is HAND")
                .since("1.0.0")
                .parser(new Parser<EquipmentSlot>() {
                    @Override
                    public EquipmentSlot parse(final @NotNull String s, final @NotNull ParseContext context) {
                        try {
                            return EquipmentSlot.valueOf(s.toUpperCase());
                        } catch(IllegalArgumentException ex) {
                            return null;
                        }
                    }

                    @Override
                    public @NotNull String toString(final EquipmentSlot b, final int flags) {
                        return b.toString();
                    }

                    @Override
                    public @NotNull String toVariableNameString(final EquipmentSlot b) {
                        return "" + b.name();
                    }
                })
                .serializer(new EnumSerializer<>(EquipmentSlot.class)));
        Classes.registerClass(new ClassInfo<>(PatternType.class, "patterntype")
                .user("pattern ?types?")
                .name("Pattern Type")
                .description("All the Pattern Types.")
                .usage(Arrays.toString(PatternType.values()))
                .examples("")
                .since("1.0.0")
                .parser(new Parser<PatternType>() {
                    @Override
                    public PatternType parse(final @NotNull String s, final @NotNull ParseContext context) {
                        try {
                            return PatternType.valueOf(s.toUpperCase());
                        } catch(IllegalArgumentException ex) {
                            return null;
                        }
                    }

                    @Override
                    public @NotNull String toString(final PatternType p, final int flags) {
                        return p.toString();
                    }

                    @Override
                    public @NotNull String toVariableNameString(final PatternType p) {
                        return "" + p.name();
                    }
                })
                .serializer(new EnumSerializer<>(PatternType.class)));
        if (Skript.classExists("com.destroystokyo.paper.event.entity.EndermanEscapeEvent.Reason")) {
            Classes.registerClass(new ClassInfo<>(Reason.class, "endermanescapereason")
                    .user("ender ?man ?escape ?reasons?")
                    .name("Enderman Escape Reason")
                    .description("All the Valid Enderman Escape Reasons")
                    .usage(Arrays.toString(Reason.values()))
                    .examples("")
                    .since("1.0.0")
                    .parser(new Parser<Reason>() {
                        @Override
                        public Reason parse(final @NotNull String s, final @NotNull ParseContext context) {
                            try {
                                return Reason.valueOf(s.toUpperCase());
                            } catch (IllegalArgumentException ex) {
                                return null;
                            }
                        }

                        @Override
                        public @NotNull String toString(final Reason r, final int flags) {
                            return r.toString();
                        }

                        @Override
                        public @NotNull String toVariableNameString(final Reason r) {
                            return "" + r.name();
                        }
                    })
                    .serializer(new EnumSerializer<>(Reason.class)));
        }
        Classes.registerClass(new ClassInfo<>(Semver.class, "version")
                .user("versions?")
                .name("Version")
                .description("A Minecraft Version")
                .usage("")
                .examples("")
                .since("1.0.0")
                .parser(new Parser<Semver>() {
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
        Classes.registerClass(new ClassInfo<>(Pose.class, "pose")
                .user("poses?")
                .name("Pose")
                .description("All the Poses.")
                .usage(Arrays.toString(Pose.values()))
                .examples("")
                .since("1.0.2")
                .parser(new Parser<Pose>() {
                    @Override
                    public Pose parse(final @NotNull String s, final @NotNull ParseContext context) {
                        try {
                            return Pose.valueOf(s.toUpperCase());
                        } catch(IllegalArgumentException ex) {
                            return null;
                        }
                    }

                    @Override
                    public @NotNull String toString(final Pose p, final int flags) {
                        return p.toString();
                    }

                    @Override
                    public @NotNull String toVariableNameString(final Pose p) {
                        return "" + p.name();
                    }
                })
                .serializer(new EnumSerializer<>(Pose.class)));
        Classes.registerClass(new ClassInfo<>(SpawnCategory.class, "spawncategory")
                .user("spawn ?categor(y|ies)")
                .name("Spawn Category")
                .description("All the Spawn Categories.")
                .usage(Arrays.toString(SpawnCategory.values()))
                .examples("")
                .since("1.0.2")
                .parser(new Parser<SpawnCategory>() {
                    @Override
                    public SpawnCategory parse(final @NotNull String s, final @NotNull ParseContext context) {
                        try {
                            return SpawnCategory.valueOf(s.toUpperCase());
                        } catch(IllegalArgumentException ex) {
                            return null;
                        }
                    }

                    @Override
                    public @NotNull String toString(final SpawnCategory s, final int flags) {
                        return s.toString();
                    }

                    @Override
                    public @NotNull String toVariableNameString(final SpawnCategory s) {
                        return "" + s.name();
                    }
                })
                .serializer(new EnumSerializer<>(SpawnCategory.class)));
        Classes.registerClass(new ClassInfo<>(BoundingBox.class, "boundingbox")
                .user("bounding ?box(es)?")
                .name("Bounding Box")
                .description("A Bounding Box")
                .usage("")
                .examples("")
                .since("1.0.2")
                .parser(new Parser<BoundingBox>() {
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
        Classes.registerClass(new ClassInfo<>(CauldronLevelChangeEvent.ChangeReason.class, "changereason")
                .user("change ?reasons?")
                .name("Cauldron Change Reason")
                .description("All the Cauldron Change Reasons.")
                .usage(Arrays.toString(CauldronLevelChangeEvent.ChangeReason.values()))
                .examples("")
                .since("1.0.2")
                .parser(new Parser<CauldronLevelChangeEvent.ChangeReason>() {
                    @Override
                    public CauldronLevelChangeEvent.ChangeReason parse(final @NotNull String s, final @NotNull ParseContext context) {
                        try {
                            return CauldronLevelChangeEvent.ChangeReason.valueOf(s.toUpperCase());
                        } catch(IllegalArgumentException ex) {
                            return null;
                        }
                    }

                    @Override
                    public @NotNull String toString(final CauldronLevelChangeEvent.ChangeReason c, final int flags) {
                        return c.toString();
                    }

                    @Override
                    public @NotNull String toVariableNameString(final CauldronLevelChangeEvent.ChangeReason c) {
                        return "" + c.name();
                    }
                })
                .serializer(new EnumSerializer<>(CauldronLevelChangeEvent.ChangeReason.class)));
        Classes.registerClass(new ClassInfo<>(EnderDragon.Phase.class, "enderdragonphase")
                .user("ender ?dragon ?phases?")
                .name("Ender Dragon Phase")
                .description("All the Ender Dragon Phases.")
                .usage(Arrays.toString(EnderDragon.Phase.values()))
                .examples("")
                .since("1.0.2")
                .parser(new Parser<EnderDragon.Phase>() {
                    @Override
                    public EnderDragon.Phase parse(final @NotNull String s, final @NotNull ParseContext context) {
                        try {
                            return EnderDragon.Phase.valueOf(s.toUpperCase());
                        } catch(IllegalArgumentException ex) {
                            return null;
                        }
                    }

                    @Override
                    public @NotNull String toString(final EnderDragon.Phase p, final int flags) {
                        return p.toString();
                    }

                    @Override
                    public @NotNull String toVariableNameString(final EnderDragon.Phase p) {
                        return "" + p.name();
                    }
                })
                .serializer(new EnumSerializer<>(EnderDragon.Phase.class)));
    }
}
