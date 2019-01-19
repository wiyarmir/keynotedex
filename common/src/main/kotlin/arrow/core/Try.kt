package arrow.core

import arrow.core.Try.Success

sealed class Try<out A> {

    companion object {
        @Suppress("TooGenericExceptionCaught")
        inline operator fun <A> invoke(f: () -> A): Try<A> =
            try {
                Success(f())
            } catch (e: Throwable) {
                Failure(e)
            }

        fun <A> raise(e: Throwable): Try<A> = Failure(e)
    }

    /**
     * Returns the given function applied to the value from this `Success` or returns this if this is a `Failure`.
     */
    inline fun <B> flatMap(f: (A) -> Try<B>): Try<B> =
        when (this) {
            is Failure -> this
            is Success -> f(value)
        }

    /**
     * Maps the given function to the value from this `Success` or returns this if this is a `Failure`.
     */
    inline fun <B> map(f: (A) -> B): Try<B> =
        flatMap { Success(f(it)) }

    /**
     * Applies `ifFailure` if this is a `Failure` or `ifSuccess` if this is a `Success`.
     */
    inline fun <B> fold(ifFailure: (Throwable) -> B, ifSuccess: (A) -> B): B =
        when (this) {
            is Failure -> ifFailure(exception)
            is Success -> ifSuccess(value)
        }

    abstract val isSuccess: Boolean

    abstract val isFailure: Boolean

    data class Success<out A>(val value: A) : Try<A>() {
        override val isFailure = false
        override val isSuccess = true
    }

    data class Failure(val exception: Throwable) : Try<Nothing>() {
        override val isFailure = true
        override val isSuccess = false
    }
}

/**
 * Returns the value from this `Success` or the given `default` argument if this is a `Failure`.
 *
 * ''Note:'': This will throw an exception if it is not a success and default throws an exception.
 */
inline fun <B> Try<B>.getOrDefault(default: () -> B): B = fold({ default() }, { it })

/**
 * Returns the value from this `Success` or the given `default` argument if this is a `Failure`.
 *
 * ''Note:'': This will throw an exception if it is not a success and default throws an exception.
 */
inline fun <B> Try<B>.getOrElse(default: (Throwable) -> B): B = fold(default, { it })

/**
 * Returns the value from this `Success` or null if this is a `Failure`.
 */
fun <B> Try<B>.orNull(): B? = getOrElse { null }

/**
 * Applies the given function `f` if this is a `Failure`, otherwise returns this if this is a `Success`.
 * This is like map for the exception.
 */
fun <B> Try<B>.recover(f: (Throwable) -> B): Try<B> = fold({ Success(f(it)) }, { Success(it) })

fun <A> (() -> A).`try`(): Try<A> = Try(this)

fun <A> A.success(): Try<A> = Success(this)

fun <A> Throwable.failure(): Try<A> = Try.Failure(this)

fun <T> Try<Try<T>>.flatten(): Try<T> = flatMap { it }
