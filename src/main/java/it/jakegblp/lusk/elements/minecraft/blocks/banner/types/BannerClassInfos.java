package it.jakegblp.lusk.elements.minecraft.blocks.banner.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import it.jakegblp.lusk.api.wrappers.EnumRegistryWrapper;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class BannerClassInfos {
    static {
        if (Classes.getExactClassInfo(PatternType.class) == null) {
            Classes.registerClass(
                    new EnumRegistryWrapper(PatternType.class, null, "pattern_type")
                            .getClassInfo("patterntype")
                            .user("pattern ?types?")
                            .name("Banner - Pattern Type")
                            .description("Represents all the available banner pattern types.")
                            .documentationId("8850")
                            .since("1.0.0, 1.2.1 (Registries), 1.3 (1.20.4 Fix)"));
        }
        if (Classes.getExactClassInfo(Pattern.class) == null) {
            Classes.registerClass(new ClassInfo<>(Pattern.class, "bannerpattern")
                        .user("banner ?patterns?")
                        .name("Banner Pattern")
                        .description("A Banner Pattern.") // add example
                        .since("1.3")
                    .parser(new Parser<>() {
                        @Override
                        public String toString(Pattern o, int flags) {
                            return "banner pattern with pattern type "+o.getPattern()+" and color "+o.getColor();
                        }

                        @Override
                        public String toVariableNameString(Pattern o) {
                            return toString(o,0) + " banner pattern (" + o + ")";
                        }

                        @Override
                        public @Nullable Pattern parse(String s, ParseContext context) {
                            return null; // todo: might add at some point, not too relevant
                        }

                        @Override
                        public boolean canParse(ParseContext context) {
                            return false;
                        }
                    }));
        }
    }
}
