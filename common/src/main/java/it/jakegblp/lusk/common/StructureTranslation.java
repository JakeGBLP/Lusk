package it.jakegblp.lusk.common;

import com.mojang.datafixers.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class StructureTranslation {

    public static <K, V> List<Pair<K, V>> fromMapToPairList(Map<K, V> map) {
        var list = new ArrayList<Pair<K, V>>();
        for (var entry : map.entrySet())
            list.add(Pair.of(entry.getKey(), entry.getValue()));
        return list;
    }

    public static <K, V> Map<K, V> fromPairListToMap(List<Pair<K, V>> pairList) {
        var map = new HashMap<K, V>();
        for (var pair : pairList)
            map.put(pair.getFirst(), pair.getSecond());
        return map;
    }

    public static <A1, B1, A2, B2> List<Pair<A1, B1>> fromMapToPairList(
            Map<A2, B2> map,
            Function<A2, A1> keyConverter,
            Function<B2, B1> valueConverter
    ) {
        List<Pair<A1, B1>> result = new ArrayList<>(map.size());
        for (Map.Entry<A2, B2> e : map.entrySet())
            result.add(Pair.of(keyConverter.apply(e.getKey()), valueConverter.apply(e.getValue())));
        return result;
    }
    public static <A1, B1, A2, B2> Map<A2, B2> fromPairListToMap(
            List<Pair<A1, B1>> list,
            Function<A1, A2> keyConverter,
            Function<B1, B2> valueConverter
    ) {
        Map<A2, B2> result = new HashMap<>(list.size());
        for (Pair<A1, B1> pair : list)
            result.put(keyConverter.apply(pair.getFirst()), valueConverter.apply(pair.getSecond()));
        return result;
    }
}
