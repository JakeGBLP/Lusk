package it.jakegblp.lusk.elements.time.types;

import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Timespan;
import it.jakegblp.lusk.api.wrappers.EnumWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class TimeClassInfos {
    static {
        if (Classes.getExactClassInfo(Timespan.TimePeriod.class) == null) {
            EnumWrapper<Timespan.TimePeriod> TIMEPERIOD_ENUM = new EnumWrapper<>(Timespan.TimePeriod.class, null, null);
            Classes.registerClass(TIMEPERIOD_ENUM.getClassInfo("timespanperiod")
                    .user("time ?span ?periods?")
                    .name("Timespan Period")
                    .description("All the Timespan Periods implemented by Skript behind the scenes.\nThis is different than https://skripthub.net/docs/?id=2166")
                    .since("1.3-beta4")
                    .parser(new Parser<>() {
                        @Override
                        @Nullable
                        public Timespan.TimePeriod parse(final @NotNull String s, final @NotNull ParseContext context) {
                            if (s.isEmpty()) return null;
                            for (Timespan.TimePeriod timePeriod : Timespan.TimePeriod.values()) {
                                if (timePeriod.name().equalsIgnoreCase(s)) return timePeriod;
                            }
                            return null;
                        }

                        @Override
                        public boolean canParse(final @NotNull ParseContext context) {
                            return true;
                        }

                        @Override
                        public @NotNull String toString(final Timespan.TimePeriod v, final int flags) {
                            return v.toString();
                        }

                        @Override
                        public @NotNull String toVariableNameString(final Timespan.TimePeriod v) {
                            return v.toString();
                        }

                        @Override
                        public @NotNull String getDebugMessage(final Timespan.TimePeriod v) {
                            return toString(v, 0) + " timespan period (" + v + ")";
                        }
                    }));
        }
    }
}
