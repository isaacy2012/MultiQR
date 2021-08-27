package com.innerCat.multiQR

import com.google.gson.Gson
import com.innerCat.multiQR.util.EnabledRegex
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTests {
    @Test
    fun item_mutation() {
        val regex = EnabledRegex("\\|")
        val item = Item("Isaac|20|135", regex)
        assertEquals(mutableListOf("Isaac", "20", "135"), item.strList)
        item.setData("Harley|20|136", regex)
        assertEquals(mutableListOf("Harley", "20", "136"), item.strList)
        val saveString = Gson().toJson(listOf(item))
        val loadItem = Gson().fromJson(saveString, Array<Item>::class.java).toMutableList()[0]
        assertEquals(item, loadItem)
    }

}