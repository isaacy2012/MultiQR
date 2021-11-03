package com.innerCat.multiQR.util

import com.innerCat.multiQR.fragments.AbstractMainActivityFragment
import kotlin.reflect.KProperty

/**
 * Nullable lazy
 *
 * @param T
 * @param initializer
 * @receiver
 * @return
 */
fun <T> nullableLazy(initializer: () -> T): NullableLazy<T> = NullableLazy(initializer)

/**
 * Nullable lazy lazily initializes an Object that might initialize as null.
 * While the value is null, it continues to call the initializer on subsequent
 * getValue() calls.
 *
 * @param T
 * @property initializer
 * @constructor Create empty Nullable lazy
 */
class NullableLazy<T>(val initializer: () -> T) {
    /**
     * Value
     */
    var value: T? = null;

    /**
     * Get value
     *
     * @param abstractMainActivityFragment
     * @param property
     * @return
     */
    operator fun getValue(
        abstractMainActivityFragment: AbstractMainActivityFragment,
        property: KProperty<*>
    ): T? {
        if (value == null) {
            value = initializer()
        }
        return value
    }
}