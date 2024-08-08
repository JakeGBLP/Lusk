package it.jakegblp.lusk.utils;

import java.util.*;

/**
 * A MultiHashMap allows multiple values to be associated with a single key.
 * It uses a HashMap internally, where each key maps to a List of values.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class MultiHashMap<K, V> {
    private Map<K, List<V>> map;
    private Map<V, K> reverseMap;

    /**
     * Constructs an empty MultiHashMap.
     */
    public MultiHashMap() {
        this.map = new HashMap<>();
        this.reverseMap = new HashMap<>();
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the new value is added to the list of existing values.
     * The reverse mapping is also updated.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the specified key
     */
    public void put(K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        reverseMap.put(value, key);
    }

    /**
     * Associates all the specified values with the specified key in this map.
     * If the map previously contained a mapping for the key, the new values are added to the list of existing values.
     * The reverse mapping is also updated.
     *
     * @param key    the key with which the specified values are to be associated
     * @param values the values to be associated with the specified key
     */
    @SafeVarargs
    public final void putAll(K key, V... values) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).addAll(List.of(values));
        for (V value : values) {
            reverseMap.put(value, key);
        }
    }

    /**
     * Returns the list of values to which the specified key is mapped,
     * or an empty list if this map contains no mapping for the key.
     *
     * @param key the key whose associated values are to be returned
     * @return the list of values to which the specified key is mapped, or an empty list if no mapping is found
     */
    public List<V> get(K key) {
        return map.getOrDefault(key, Collections.emptyList());
    }

    /**
     * Returns the key to which the specified value is mapped,
     * or null if this map contains no mapping for the value.
     *
     * @param value the value whose associated key is to be returned
     * @return the key to which the specified value is mapped, or null if no mapping is found
     */
    public K getKey(V value) {
        return reverseMap.get(value);
    }

    /**
     * Checks if the map contains the specified key.
     *
     * @param key the key whose presence in this map is to be tested
     * @return true if this map contains a mapping for the specified key, false otherwise
     */
    public boolean hasKey(K key) {
        return map.containsKey(key);
    }

    /**
     * Returns the key to which the specified value is mapped,
     * or the specified default key if this map contains no mapping for the value.
     *
     * @param value the value whose associated key is to be returned
     * @param defaultKey the default key to return if no mapping is found
     * @return the key to which the specified value is mapped, or the default key if no mapping is found
     */
    public K getKeyOrDefault(V value, K defaultKey) {
        return reverseMap.getOrDefault(value, defaultKey);
    }

    /**
     * Removes a single instance of the specified value from the list of values associated with the specified key.
     * If the value is the last one associated with the key, the key is also removed from the map.
     * The reverse mapping is also updated.
     *
     * @param key   the key from which the specified value is to be removed
     * @param value the value to be removed from the list of values associated with the key
     * @return true if the value was successfully removed, false if the value was not found
     */
    public boolean remove(K key, V value) {
        List<V> values = map.get(key);
        if (values != null && values.remove(value)) {
            reverseMap.remove(value);
            if (values.isEmpty()) {
                map.remove(key);
            }
            return true;
        }
        return false;
    }

    /**
     * Removes all values associated with the specified key from this map.
     * The reverse mappings are also removed.
     *
     * @param key the key whose associated values are to be removed
     */
    public void removeAll(K key) {
        List<V> values = map.remove(key);
        if (values != null) {
            for (V value : values) {
                reverseMap.remove(value);
            }
        }
    }

    /**
     * Returns a Set view of the keys contained in this map.
     *
     * @return a Set view of the keys contained in this map
     */
    public Set<K> keySet() {
        return map.keySet();
    }

    /**
     * Returns a Set view of the mappings contained in this map.
     * Each entry in the set is a mapping from a key to a list of values.
     *
     * @return a Set view of the mappings contained in this map
     */
    public Set<Map.Entry<K, List<V>>> entrySet() {
        return map.entrySet();
    }

    /**
     * Returns the number of keys in this map.
     *
     * @return the number of keys in this map
     */
    public int size() {
        return map.size();
    }

    /**
     * Returns true if this map contains no key-value mappings.
     *
     * @return true if this map contains no key-value mappings, false otherwise
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }
}