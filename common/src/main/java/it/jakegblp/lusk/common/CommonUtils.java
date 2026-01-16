package it.jakegblp.lusk.common;

import it.jakegblp.lusk.common.reflection.SimpleArray;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class CommonUtils {

    public static <F, C extends Collection<F>> Function<Collection<F>, C> safeCast(Function<Collection<F>, C> castFunction) {
        return castFunction;
    }

    public static PublicKey byteToPublicKey(byte[] encodedKey) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encodedKey));
        } catch (Exception var3) {
            throw new IllegalArgumentException(var3);
        }
    }

    public static int randomId() {
        return ThreadLocalRandom.current().nextInt(999999, Integer.MAX_VALUE);
    }

    public static byte packDegrees(float degrees) {
        return (byte) Math.floor(degrees * 256.0F / 360.0F);
    }

    public static float unpackDegrees(byte degrees) {
        return (float) (degrees * 360) / 256.0F;
    }

    public static long packInts(int x, int y, int z) {
        return ((long) x & 67108863L) << 38 | (long) y & 4095L | ((long) z & 67108863L) << 12;
    }

    public static double lowPrecision(double d) {
        return Double.isNaN(d) ? 0 : Math.max(-1.7179869183E10, Math.min(d, 1.7179869183E10));
    }

    public static long pack(double d) {
        return Math.round((d * 0.5 + 0.5) * 32766.0);
    }

    public static double unpack(long i) {
        return Math.min(i & 32767L, 32766.0) * 2.0 / 32766.0 - 1.0;
    }

    public static long packBlockVector(BlockVector blockVector) {
        return packInts(blockVector.getBlockX(), blockVector.getBlockX(), blockVector.getBlockZ());
    }

    public static BlockVector unpackBlockVector(long packedBlockVector) {
        return new BlockVector((int) (packedBlockVector >> 38), (int) (packedBlockVector << 52 >> 52), (int) (packedBlockVector << 26 >> 38));
    }

    /**
     * @param compactUUID a uuid without dashes, must have a length of 32.
     * @throws IllegalArgumentException if the provided string is not 32 characters long
     * @return the respective UUID
     */
    public static @NotNull UUID fromCompactUUID(@NotNull String compactUUID) {
        if (compactUUID.length() != 32)
            throw new IllegalArgumentException("Invalid compact UUID length: " + compactUUID.length());
        long mostSignificantBits = 0;
        long leastSignificantBits = 0;
        for (int characterIndex = 0; characterIndex < 32; characterIndex++) {
            char hexCharacter = compactUUID.charAt(characterIndex);
            int hexValue = (hexCharacter <= '9' ? hexCharacter - '0' : (hexCharacter <= 'F' ? hexCharacter - 'A' + 10 : hexCharacter - 'a' + 10));
            if (characterIndex < 16)
                mostSignificantBits = (mostSignificantBits << 4) | hexValue;
            else
                leastSignificantBits = (leastSignificantBits << 4) | hexValue;
        }
        return new UUID(mostSignificantBits, leastSignificantBits);
    }

    /**
     * @param uuid a uuid
     * @return the respective compact UUID
     */
    public static @NotNull String toCompactUUID(@NotNull UUID uuid) {
        char[] compactChars = new char[32];
        long mostSignificantBits = uuid.getMostSignificantBits();
        long leastSignificantBits = uuid.getLeastSignificantBits();
        for (int characterIndex = 31; characterIndex >= 0; characterIndex--) {
            int hexValue = (int) ((characterIndex >= 16 ? leastSignificantBits : mostSignificantBits) & 0xF);
            compactChars[characterIndex] = (char) (hexValue < 10 ? '0' + hexValue : 'a' + hexValue - 10);
            if (characterIndex >= 16) leastSignificantBits >>>= 4;
            else mostSignificantBits >>>= 4;
        }
        return new String(compactChars);
    }

    public static <T> @Nullable T tryOrNull(Supplier<? extends T> supplier) {
        return tryOrDefault(supplier, null);
    }

    public static <T> @Nullable T tryOrDefault(Supplier<? extends T> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static boolean isBase64(@NotNull String base64) {
        return fromBase64(base64) != null;
    }

    public static @Nullable String fromBase64(@NotNull String base64) {
        return fromBase64(base64, StandardCharsets.UTF_8);
    }

    public static @Nullable String fromBase64(@NotNull String base64, @NotNull Charset charset) {
        return tryOrNull(() -> new String(Base64.getDecoder().decode(base64), charset));
    }

    public static <E extends Enum<E>> @Nullable E safeValueOf(@NotNull Class<E> enumClass, String name) {
        if (name == null) return null;
        for (E e : enumClass.getEnumConstants())
            if (e.name().equalsIgnoreCase(name))
                return e;
        return null;
    }

    public static <F> @Nullable F findFirst(@Nullable F @NotNull [] objects, @NotNull Predicate<@NotNull F> predicate) {
        for (F object : objects) {
            if (object != null && predicate.test(object))
                return object;
        }
        return null;
    }

    public static <T> @NotNull List<@NotNull T> nonNull(@NotNull List<@Nullable T> list) {
        List<T> result = new ArrayList<>(list.size());
        for (T value : list)
            if (value != null)
                result.add(value);
        return result;
    }

    public static <T> @NotNull T[] nonNull(@NotNull Class<T> tClass, @Nullable T @NotNull [] array) {
        int count = 0;
        for (T value : array)
            if (value != null)
                count++;
        T[] result = SimpleArray.create(tClass, count);
        int index = 0;
        for (T value : array)
            if (value != null)
                result[index++] = value;
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <F, T> List<T> filterToList(Class<T> type, F[] objects) {
        List<T> list = new ArrayList<>(objects.length);
        for (Object o : objects)
            if (type.isInstance(o))
                list.add((T) o);
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <F, T> List<T> filter(Class<T> type, Collection<F> objects) {
        List<T> list = new ArrayList<>(objects.size());
        for (F o : objects)
            if (type.isInstance(o))
                list.add((T) o);
        return list;
    }

    public static <F, T> T[] filter(Class<T> type, F[] objects) {
        int count = 0;
        for (F obj : objects)
            if (type.isInstance(obj))
                count++;
        T[] result = SimpleArray.create(type, count);
        int idx = 0;
        for (F obj : objects)
            if (type.isInstance(obj))
                result[idx++] = type.cast(obj);

        return result;
    }

    public static <F> F[] filter(F[] objects, Predicate<F> predicate) {
        int count = 0;
        for (F obj : objects)
            if (predicate.test(obj))
                count++;

        F[] result = SimpleArray.create(SimpleArray.getComponentType(objects), count);
        int i = 0;
        for (F obj : objects)
            if (predicate.test(obj))
                result[i++] = obj;

        return result;
    }


    @SuppressWarnings("unchecked")
    public static <F, T> List<T> filterMapToList(
            @NotNull Class<F> type,
            @Nullable Object @NotNull [] objects,
            @NotNull Function<? super @NotNull F, ? extends T> converter) {
        List<T> list = new ArrayList<>(objects.length);
        for (Object object : objects)
            if (type.isInstance(object))
                list.add(converter.apply((F) object));
        return list;
    }

    public static <F, T> @NotNull List<@NotNull T> map(
            @NotNull Collection<? extends @Nullable F> values,
            @NotNull Function<? super @NotNull F, ? extends @Nullable T> converter
    ) {
        var list = new ArrayList<T>(values.size());
        for (F value : values)
            if (value != null) {
                var converted = converter.apply(value);
                if (converted != null)
                    list.add(converted);
            }
        return list;
    }

    public static <F, T> @NotNull List<T> mapToList(
            @Nullable F @NotNull [] values,
            @NotNull Function<? super @NotNull F, ? extends T> converter
    ) {
        var list = new ArrayList<T>(values.length);
        for (F value : values)
            if (value != null)
                list.add(converter.apply(value));
        return list;
    }

    public static <F, T> @NotNull Object[] mapToObjectArray(
            @NotNull List<? extends @Nullable F> values,
            @NotNull Function<? super @NotNull F, ? extends T> converter
    ) {
        int count = 0;
        for (F f : values)
            if (f != null)
                count++;

        Object[] newArray = new Object[count];
        int idx = 0;
        for (F f : values)
            if (f != null)
                newArray[idx++] = converter.apply(f);

        return newArray;
    }

    public static <F, T> @NotNull T[] mapToArray(
            @NotNull Class<T> type,
            @NotNull List<? extends @Nullable F> values,
            @NotNull Function<? super @NotNull F, ? extends T> converter
    ) {
        int count = 0;
        for (F f : values)
            if (f != null)
                count++;

        T[] newArray = SimpleArray.create(type, count);
        int idx = 0;
        for (F f : values)
            if (f != null)
                newArray[idx++] = type.cast(converter.apply(f));

        return newArray;
    }

    public static <F, T> @NotNull T[] map(
            @Nullable F @NotNull [] values,
            @NotNull Function<? super @NotNull F, ? extends T> converter
    ) {
        return map(null, values, converter);
    }

    @SuppressWarnings("unchecked")
    public static <F, T> @NotNull T[] map(
            @Nullable Class<T> type,
            @Nullable F @NotNull [] values,
            @NotNull Function<? super @NotNull F, ? extends T> converter
    ) {
        int count = 0;
        for (F f : values)
            if (f != null)
                count++;

        T[] newArray = (T[]) ((type == null || type == Object.class) ? new Object[count] : SimpleArray.create(type, count));
        int idx = 0;
        for (F f : values)
            if (f != null)
                newArray[idx++] = type == null ? converter.apply(f) : type.cast(converter.apply(f));

        return newArray;
    }


    public static <F, T> @NotNull List<@NotNull T> flatMap(
            @NotNull Collection<? extends @Nullable F> values,
            @NotNull Function<? super @NotNull F, ? extends @Nullable Collection<? extends @Nullable T>> converter
    ) {
        var list = new ArrayList<T>(values.size());
        for (F value : values) {
            if (value == null) continue;
            var result = converter.apply(value);
            if (result == null) continue;
            for (T t : result)
                if (t != null)
                    list.add(t);
        }
        return list;
    }

    public static <F, T> @NotNull List<@NotNull T> flatMapToList(
            @Nullable F @NotNull [] values,
            @NotNull Function<? super @NotNull F, ? extends @Nullable Collection<? extends @Nullable T>> converter
    ) {
        return flatMap(Arrays.asList(values), converter);
    }

    @SuppressWarnings("unchecked")
    public static <F, T, R> @NotNull R[] flatMap(
            @NotNull Class<R> type,
            @Nullable F @NotNull [] values,
            @NotNull Function<? super @NotNull F, ? extends @Nullable Collection<? extends @Nullable T>> toCollection,
            @Nullable Function<? super @NotNull T, ? extends @Nullable R> mapper
    ) {
        int len = values.length;
        List<R> results = new ArrayList<>(len);

        for (F v : values) {
            if (v == null) continue;

            Collection<? extends @Nullable T> collection = toCollection.apply(v);
            if (collection == null || collection.isEmpty()) continue;

            for (T element : collection) {
                if (element == null) continue;

                R mapped = (mapper != null) ? mapper.apply(element) : (R) element;
                if (type.isInstance(mapped))
                    results.add(mapped);
            }
        }

        R[] array = SimpleArray.create(type, results.size());
        return results.toArray(array);
    }

    public static <F, T> @NotNull T[] flatMap(
            @NotNull Class<T> type,
            @Nullable F @NotNull [] values,
            @NotNull Function<? super @NotNull F, ? extends @Nullable Collection<? extends @Nullable T>> toCollection
    ) {
        return flatMap(type, values, toCollection, null);
    }

    public static <T> @NotNull Set<T> toSet(@NotNull T @NotNull [] values) {
        var set = new HashSet<T>((int) Math.ceil(values.length / 0.75d));
        Collections.addAll(set, values);
        return set;
    }

    public static <E extends Enum<E>> @NotNull EnumSet<E> copyOrEmptyEnumSet(@NotNull Collection<E> c, @NotNull Class<E> enumClass) {
        return c.isEmpty() ? EnumSet.noneOf(enumClass) : EnumSet.copyOf(c);
    }

}
