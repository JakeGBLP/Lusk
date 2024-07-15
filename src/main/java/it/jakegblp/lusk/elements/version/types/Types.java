package it.jakegblp.lusk.elements.version.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import com.vdurmont.semver4j.Semver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

import static it.jakegblp.lusk.utils.Utils.Version;

public class Types {
    public static final Pattern VERSION_PATTERN = Pattern.compile("^\\d+\\.\\d+(\\.\\d+)?[-+a-zA-Z0-9.]*$");
    static {
        if (Classes.getExactClassInfo(Semver.class) == null)
            Classes.registerClass(new ClassInfo<>(Semver.class, "version")
                    .user("versions?")
                    .name("Version")
                    .description("A Minecraft Version.")
                    .usage("")
                    .examples("") // add example
                    .after("number","long","integer","double","float","short","byte")
                    .since("1.0.0, 1.2 (without strings)")
                    .parser(new Parser<>() {
                        @Override
                        @Nullable
                        public Semver parse(final @NotNull String s, final @NotNull ParseContext context) {
                            if (s.isEmpty()) return null;
                            if (VERSION_PATTERN.matcher(s).matches()) return Version(s);
                            return null;
                        }

                        @Override
                        public boolean canParse(final @NotNull ParseContext context) {
                            return true;
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
