/**
 * @author Isaac Young
 */
package com.innerCat.multiQR.factories

import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText

/**
 * Gets Manual Entry TextWatcher.
 *
 * @param editText editText the Manual Entry editText
 * @param okButton    the ok button
 * @return the Manual Entry
 */
fun getManualAddTextWatcher(editText: EditText, okButton: Button): TextWatcher {
    return object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val trimmedRefillInput = editText.text.toString().trim { it <= ' ' }
            val manualInputInputPass = trimmedRefillInput.isNotEmpty()
            okButton.isEnabled = manualInputInputPass
        }

        override fun afterTextChanged(s: Editable) {}
    }
}
