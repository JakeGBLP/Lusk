package it.jakegblp.lusk.elements.math.eulerangle.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import it.jakegblp.lusk.utils.VectorUtils;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.StreamCorruptedException;

import static it.jakegblp.lusk.utils.CompatibilityUtils.registerConverter;
import static it.jakegblp.lusk.utils.LuskUtils.toSkriptConfigNumberAccuracy;

public class EulerAngleClassInfos {
    static {

        if (Classes.getExactClassInfo(EulerAngle.class) == null) {
            Classes.registerClass(new ClassInfo<>(EulerAngle.class, "eulerangle")
                    .user("euler ?angles?")
                    .name("Euler Angle")
                    .description("EulerAngle is used to represent 3 angles, one for each axis (x, y, z). The angles are in radians") // add example
                    .since("1.3")
                    .parser(new Parser<>() {
                        @Override
                        @Nullable
                        public EulerAngle parse(final @NotNull String s, final @NotNull ParseContext context) {
                            return null;
                        }

                        @Override
                        public boolean canParse(final @NotNull ParseContext context) {
                            return false;
                        }

                        @Override
                        public @NotNull String toString(final EulerAngle b, final int flags) {
                            return toSkriptConfigNumberAccuracy(b.toString());
                        }

                        @Override
                        public @NotNull String toVariableNameString(final EulerAngle b) {
                            return b.toString();
                        }

                        @Override
                        public @NotNull String getDebugMessage(final EulerAngle b) {
                            return toString(b, 0) + " euler angle (" + b + ")";
                        }
                    })
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
                            assert false;
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
                    }));
            registerConverter(EulerAngle.class, Vector.class, VectorUtils::toVector);
            registerConverter(Vector.class, EulerAngle.class, VectorUtils::toEulerAngle);
        }
    }
}
