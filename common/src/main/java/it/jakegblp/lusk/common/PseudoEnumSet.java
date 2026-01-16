package it.jakegblp.lusk.common;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

@Getter
public final class PseudoEnumSet<E extends PseudoEnum> extends AbstractSet<E> implements Iterable<E>, Copyable<PseudoEnumSet<E>> {

    private final Class<E> elementType;
    private final long[] bits;

    private PseudoEnumSet(Class<E> elementType, long[] bits) {
        this.elementType = elementType;
        this.bits = bits;
    }

    public static <E extends PseudoEnum> @NotNull PseudoEnumSet<E> noneOf(Class<E> type) {
        int size = PseudoEnum.values(type).length;
        return new PseudoEnumSet<>(type, new long[wordCount(size)]);
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <E extends PseudoEnum> @NotNull PseudoEnumSet<E> of(E... elements) {
        if (elements.length == 0) throw new IllegalArgumentException("Empty PseudoEnumSet.of()");
        Class<E> type = (Class<E>) elements[0].getClass();
        PseudoEnumSet<E> set = noneOf(type);
        set.addAll(Arrays.asList(elements));
        return set;
    }

    @SuppressWarnings("unchecked")
    public static <E extends PseudoEnum> @NotNull PseudoEnumSet<E> of(@NotNull Collection<E> elements) {
        if (elements.isEmpty())
            throw new IllegalArgumentException("Empty PseudoEnumSet.of(collection)");
        Iterator<E> it = elements.iterator();
        E first = it.next();
        Class<E> type = (Class<E>) first.getClass();
        PseudoEnumSet<E> set = noneOf(type);
        set.add(first);
        while (it.hasNext())
            set.add(it.next());
        return set;
    }


    public static <E extends PseudoEnum> @NotNull PseudoEnumSet<E> copyOf(PseudoEnumSet<E> other) {
        return new PseudoEnumSet<>(other.elementType, other.bits.clone());
    }

    public boolean add(E e) {
        validate(e);
        return setBit(e.ordinal());
    }

    public boolean addAll(Collection<? extends E> collection) {
        boolean changed = false;
        for (E e : collection)
            if (add(e)) changed = true;
        return changed;
    }

    public boolean remove(E e) {
        validate(e);
        return clearBit(e.ordinal());
    }

    public boolean removeAll(Collection<?> collection) {
        boolean changed = false;
        for (Object o : collection)
            if (remove(o)) changed = true;
        return changed;
    }

    public boolean contains(E e) {
        validate(e);
        return testBit(e.ordinal());
    }

    public int size() {
        return bitCount();
    }

    public boolean isEmpty() {
        return bitCount() == 0;
    }

    public void clear() {
        Arrays.fill(bits, 0);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        E[] values = PseudoEnum.values(elementType);
        return new Iterator<>() {
            int next = nextSetBit(0);

            @Override
            public boolean hasNext() {
                return next >= 0;
            }

            @Override
            public E next() {
                if (next < 0) throw new NoSuchElementException();
                E value = values[next];
                next = nextSetBit(next + 1);
                return value;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        for (E e : this) action.accept(e);
    }

    @Override
    public String toString() {
        List<String> names = new ArrayList<>();
        for (E e : this) names.add(e.name());
        return elementType.getSimpleName() + setString(names);
    }

    private static int wordCount(int bits) {
        return (bits + 63) >>> 6;
    }

    private boolean setBit(int bit) {
        int idx = bit >>> 6;
        long mask = 1L << bit;
        boolean was = (bits[idx] & mask) != 0;
        bits[idx] |= mask;
        return !was;
    }

    private boolean clearBit(int bit) {
        int idx = bit >>> 6;
        long mask = 1L << bit;
        boolean was = (bits[idx] & mask) != 0;
        bits[idx] &= ~mask;
        return was;
    }

    private boolean testBit(int bit) {
        return (bits[bit >>> 6] & (1L << bit)) != 0;
    }

    private int nextSetBit(int from) {
        int wordIndex = from >>> 6;
        if (wordIndex >= bits.length) return -1;
        long word = bits[wordIndex] & (~0L << from);
        while (word == 0) {
            if (++wordIndex >= bits.length) return -1;
            word = bits[wordIndex];
        }
        return (wordIndex << 6) + Long.numberOfTrailingZeros(word);
    }

    private int bitCount() {
        int count = 0;
        for (long w : bits) count += Long.bitCount(w);
        return count;
    }

    private void validate(E e) {
        if (e.getClass() != elementType)
            throw new ClassCastException("Cannot use " + e + " in PseudoEnumSet<" + elementType.getSimpleName() + ">");
    }

    private static String setString(@NotNull List<String> names) {
        return names.toString();
    }

    @Contract(" -> new")
    @Override
    public @NotNull PseudoEnumSet<E> copy() {
        return new PseudoEnumSet<>(elementType, bits.clone());
    }
}
