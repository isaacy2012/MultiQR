package com.innerCat.multiQR

import com.innerCat.multiQR.util.OptionalRegex

class Item(dataString: String, splitRegex: OptionalRegex) {

    fun setData(dataString: String, regex: OptionalRegex) {
        strList = regex.split(dataString)
    }

    var strList: MutableList<String>

    init {
        strList = splitRegex.split(dataString)
    }



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (strList != other.strList) return false

        return true
    }

    override fun hashCode(): Int {
        return strList.hashCode()
    }

    override fun toString(): String {
        return "Item(strList=$strList)"
    }

}