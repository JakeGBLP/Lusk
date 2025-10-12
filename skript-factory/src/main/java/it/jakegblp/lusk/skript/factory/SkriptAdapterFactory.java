package it.jakegblp.lusk.skript.factory;

import it.jakegblp.lusk.common.Version;
import it.jakegblp.lusk.skript.core.adapters.SkriptAdapter;
import it.jakegblp.lusk.skript.impl.from_2_12_2.From_2_12_2;
import it.jakegblp.lusk.skript.impl.to_2_12_1.To_2_12_1;

public class SkriptAdapterFactory {

    public static SkriptAdapter createSkriptAdapter(
            Version skriptVersion
    ) {
        if (skriptVersion.isGreaterOrEqual(Version.of(2, 12, 2)))
            return new From_2_12_2();
        return new To_2_12_1();
    }
}
