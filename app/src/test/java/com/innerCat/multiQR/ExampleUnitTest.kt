package com.innerCat.multiQR

import com.innerCat.multiQR.util.listToString
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testing_StringJoin() {
        val list = ArrayList<String>()
        list.add("A")
        list.add("B")
        list.add("C")
        list.add("D")
        list.add("E")

        assertEquals("A, B, C, D, E", listToString(list))
    }
}