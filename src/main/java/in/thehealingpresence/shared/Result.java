package in.thehealingpresence.shared;

import java.util.function.Function;

/**
 * Algebraic Result type — explicit Success / Failure variant instead of throwing
 * exceptions for control-flow. Used by {@link in.thehealingpresence.scheduling.SlotSchedulerService}
 * to model "the booking was rejected for a known business reason" as a value
 * rather than an exception (which is reserved for truly unexpected conditions).
 *
 * <p>Sealed interface + two record subtypes — pattern-matching with
 * {@code switch (result)} resolves both arms exhaustively at compile time.
 *
 * <pre>{@code
 * Result<Booking, BookingFailure> r = scheduler.tryBook(dto);
 * switch (r) {
 *   case Result.Success<Booking, BookingFailure>(Booking b) -> redirectToDay(b.slotStart());
 *   case Result.Failure<Booking, BookingFailure>(BookingFailure f) -> showError(f.userMessage());
 * }
 * }</pre>
 *
 * @param <T> Success payload type.
 * @param <E> Failure code type.
 */
public sealed interface Result<T, E> permits Result.Success, Result.Failure {

    static <T, E> Result<T, E> success(T value) { return new Success<>(value); }
    static <T, E> Result<T, E> failure(E error) { return new Failure<>(error); }

    default boolean isSuccess() { return this instanceof Success<T, E>; }
    default boolean isFailure() { return this instanceof Failure<T, E>; }

    /** @throws IllegalStateException if this is a Failure. */
    default T value() {
        if (this instanceof Success<T, E> s) return s.value();
        throw new IllegalStateException("Result is a Failure: " + ((Failure<T, E>) this).error());
    }

    /** @throws IllegalStateException if this is a Success. */
    default E error() {
        if (this instanceof Failure<T, E> f) return f.error();
        throw new IllegalStateException("Result is a Success");
    }

    /** Apply {@code fn} to the success value, or pass the failure through unchanged. */
    default <U> Result<U, E> map(Function<? super T, ? extends U> fn) {
        return switch (this) {
            case Success<T, E> s -> Result.success(fn.apply(s.value()));
            case Failure<T, E> f -> Result.failure(f.error());
        };
    }

    record Success<T, E>(T value) implements Result<T, E> {}
    record Failure<T, E>(E error) implements Result<T, E> {}
}
