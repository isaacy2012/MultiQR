package com.innerCat.multiQR.util

interface OptionalRegex {
    /**
     * Whether a string passes this OptionalRegex
     * @param str The string to match
     */
    fun passes(str: String): Boolean

    fun split(str: String): List<String>
}

class EnabledRegex(regexString: String) : OptionalRegex {
    val regex: Regex = Regex(regexString)

    /**
     * Checks that the string matches the regex
     * @param str The string to match
     */
    override fun passes(str: String): Boolean {
        return regex.matches(str)
    }

    override fun split(str: String): List<String> {
        return regex.split(str)
    }
}

class DisabledRegex : OptionalRegex {
    /**
     * Returns true since matching is disabled
     * @param str The string to match
     */
    override fun passes(str: String): Boolean {
        return true
    }

    override fun split(str: String): List<String> {
        return listOf(str)
    }
}
