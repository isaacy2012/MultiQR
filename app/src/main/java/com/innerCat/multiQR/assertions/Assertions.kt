package com.innerCat.multiQR.assertions

import com.innerCat.multiQR.BuildConfig

fun assert(predicate: Boolean) {
    if (BuildConfig.DEBUG && predicate == false) {
        error("Assertion failed")
    }
}

fun assert(predicate: Boolean, message: String) {
    if (BuildConfig.DEBUG && predicate == false) {
        error(message)
    }

}