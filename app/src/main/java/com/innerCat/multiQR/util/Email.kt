/**
 * @author Nathanael Rais, Isaac Young
 */
package com.innerCat.multiQR.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.innerCat.multiQR.Item


const val dateString: String = "\$date"
/**
 * Email Intent to send the adapter
 *
 * @param context - content to output to
 * @param itemList - list to output
 */
fun sendEmail(context: Context, itemList: List<Item>, address: String?, subject: String?) {
    // generate a selector to point the intent to ACTION_SENDTO
    val emailSelectorIntent = Intent(Intent.ACTION_SENDTO)
    emailSelectorIntent.data = Uri.parse("mailto:")

    // generate intent to contain "EXTRA"s which are the address, subject, body of the email
    val emailIntent = Intent(Intent.ACTION_SEND)

    // if address exists then set the address
    address?.let {
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
    }
    // if subject exists then set the subject
    subject?.let {
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject.replace(dateString, getCurrentTimeString()))
    }
    emailIntent.putExtra(Intent.EXTRA_TEXT, listToEmailString(itemList))

    emailIntent.selector = emailSelectorIntent  // assign selector

    try {
        context.startActivity(Intent.createChooser(emailIntent, "Send Email"))
    } catch (ex: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "There are no email clients installed.",
            Toast.LENGTH_SHORT
        ).show()
    }
}
