package it.jakegblp.lusk.skript.api.syntax.effect;

import ch.njol.skript.util.Timespan;
import it.jakegblp.lusk.skript.utils.AddonUtils;
import org.jetbrains.annotations.NotNull;
import org.skriptlang.skript.registration.SyntaxRegistry;

import java.util.function.BiFunction;

public abstract class SimpleTimeMetadataEffect extends BiTypeMetadataEffect<Integer, Timespan>{

    public static <E extends SimpleMetadataEffect<?>> void register(
            SyntaxRegistry syntaxRegistry,
            Class<E> effectClass,
            BiFunction<String, String, E> instantiator,
            String firstTypePropertyName,
            String secondTypePropertyName
    ) {
        AddonUtils.registerEffect(syntaxRegistry, effectClass, () -> instantiator.apply(firstTypePropertyName, secondTypePropertyName),
                "(set [fake]|fake) " + firstTypePropertyName + " of %protocolentityreferences% to %integer% for %players%",
                "(set [fake]|fake) " + secondTypePropertyName + " of %protocolentityreferences% to %timespan% for %players%"
        );
    }

    public SimpleTimeMetadataEffect(String firstTypePropertyName, String secondTypePropertyName) {
        super(firstTypePropertyName, secondTypePropertyName);
    }

    @Override
    public @NotNull Integer asFirstType(Timespan secondType) {
        return Math.toIntExact(secondType.getAs(Timespan.TimePeriod.TICK));
    }
}
