package me.jake.lusk.elements.classes;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumSerializer;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import me.jake.lusk.classes.Version;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.StonecuttingRecipe;
import org.jetbrains.annotations.NotNull;
import com.destroystokyo.paper.event.entity.EndermanEscapeEvent.Reason;
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
        Classes.registerClass(new ClassInfo<>(Version.class, "version")
                .user("versions?")
                .name("Version")
                .description("A Minecraft Version")
                .usage("")
                .examples("")
                .since("1.0.0")
                .parser(new Parser<Version>() {
                    @Override
                    @Nullable
                    public Version parse(final @NotNull String s, final @NotNull ParseContext context) {
                        return Version.parse(s);
                    }

                    @Override
                    public boolean canParse(final @NotNull ParseContext context) {
                        return true;
                    }

                    @Override
                    public @NotNull String toString(final Version v, final int flags) {
                        return v.toString();
                    }

                    @Override
                    public @NotNull String toVariableNameString(final Version v) {
                        return v.toString();
                    }

                    @Override
                    public @NotNull String getDebugMessage(final Version v) {
                        return toString(v, 0) + " version (" + v.toString() + ")";
                    }
                }));
    }
}
