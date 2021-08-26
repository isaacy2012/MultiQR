package com.innerCat.multiQR

import com.innerCat.multiQR.util.OptionalRegex

class Item(private var _dataString: String, splitRegex: OptionalRegex) {
    val dataString: String
    get() {
        return _dataString
    }

    fun setDataString(dataString: String, regex: OptionalRegex) {
        _dataString = dataString
        strList = regex.split(dataString)
    }


    var strList: List<String>

    init {
        strList = splitRegex.split(dataString)
    }

    /**
     * Refresh the list with the new regex
     */
    fun updateRegex(splitRegex: OptionalRegex) {
        setDataString(dataString, splitRegex)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (_dataString != other._dataString) return false
        if (strList != other.strList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _dataString.hashCode()
        result = 31 * result + strList.hashCode()
        return result
    }

}