package it.jakegblp.lusk.elements.time.types;

import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Timespan;
import it.jakegblp.lusk.api.skript.EnumWrapper;

import static it.jakegblp.lusk.utils.Constants.SKRIPT_HAS_TIMESPAN_TIMEPERIOD;

@SuppressWarnings("unused")
public class TimeClassInfos {
    static {
        if (SKRIPT_HAS_TIMESPAN_TIMEPERIOD && Classes.getExactClassInfo(Timespan.TimePeriod.class) == null) {
            EnumWrapper<Timespan.TimePeriod> TIMEPERIOD_ENUM = new EnumWrapper<>(Timespan.TimePeriod.class, null, null);
            Classes.registerClass(TIMEPERIOD_ENUM.getClassInfo("timespanperiod")
                    .user("time ?span ?periods?")
                    .name("Timespan Period")
                    .description("All the Timespan Periods implemented by Skript behind the scenes.\nThis is different than https://skripthub.net/docs/?id=2166")
                    .since("1.3"));
        }
    }
}
