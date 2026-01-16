package it.jakegblp.lusk.common;

import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public sealed interface Either<L, R> permits Either.Left, Either.Neither, Either.Right {
    static <L, R> Either<L, R> left(L value) {
        return new Left<>(value);
    }

    static <L, R> Either<L, R> right(R value) {
        return new Right<>(value);
    }

    static <L, R> Neither<L, R> neither() {
        return new Neither<>();
    }

    boolean isLeft();

    boolean isRight();

    default boolean isNeither() {
        return !isLeft() && !isRight();
    }

    L leftOrThrow();

    R rightOrThrow();

    @Nullable L left();

    @Nullable R right();

    default <T> T mapEither(Function<L, T> onLeft, Function<R, T> onRight) {
        return switch (this) {
            case Left<L, R> l -> onLeft.apply(l.value());
            case Right<L, R> r -> onRight.apply(r.value());
            default -> null;
        };
    }

    record Left<L, R>(L value) implements Either<L, R> {
        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public L leftOrThrow() {
            return value;
        }

        @Override
        public R rightOrThrow() {
            throw new IllegalStateException("Expected right, but was left: " + value);
        }

        @Override
        public @Nullable L left() {
            return value;
        }

        @Override
        public @Nullable R right() {
            return null;
        }
    }

    record Right<L, R>(R value) implements Either<L, R> {
        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return true;
        }

        @Override
        public L leftOrThrow() {
            throw new IllegalStateException("Expected left, but was right: " + value);
        }

        @Override
        public R rightOrThrow() {
            return value;
        }

        @Override
        public @Nullable L left() {
            return null;
        }

        @Override
        public @Nullable R right() {
            return value;
        }
    }

    record Neither<L, R>() implements Either<L, R> {

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public boolean isRight() {
            return false;
        }

        @Override
        public L leftOrThrow() {
            throw new IllegalStateException("Expected left, but neither is present");
        }

        @Override
        public R rightOrThrow() {
            throw new IllegalStateException("Expected right, but neither is present");
        }

        @Override
        public @Nullable L left() {
            return null;
        }

        @Override
        public @Nullable R right() {
            return null;
        }
    }

}

