package it.jakegblp.lusk.elements.version.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.vdurmont.semver4j.Semver;
import org.jetbrains.annotations.NotNull;

public class Types {
    static {
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
    }
}
