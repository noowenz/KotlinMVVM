package au.com.official.nwz.utils.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import au.com.official.nwz.R
import java.math.BigDecimal

fun Context.showAlert(message: String) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle(R.string.app_name)
            .setMessage(message)
            .setPositiveButton(R.string.tv_ok) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
}

fun AppCompatActivity.showNotCancellableAlert(message: String) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle(R.string.app_name)
            .setMessage(message)
            .setPositiveButton(R.string.tv_ok) { dialog, _ ->
                finish()
                transitionActivityFinish()
            }
    val alertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialog.show()
}

fun Context.showNotCancellableAlert(message: String, callback: AlertDialogCallback) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle(R.string.app_name)
            .setMessage(message)
            .setPositiveButton(R.string.tv_ok) { dialog, _ ->
                dialog.dismiss()
                callback.onPositiveButtonClicked()
            }
    val alertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialog.show()
}

fun Context.showConfirmationDialog(callback: AlertDialogCallback, message: String, positiveButton: String = "OK", negativeButton: String = "Cancel") {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle(R.string.app_name)
            .setMessage(message)
            .setPositiveButton(positiveButton) { dialog, _ ->
                dialog.dismiss()
                callback.onPositiveButtonClicked()
            }
            .setNegativeButton(negativeButton) { dialog, _ ->
                dialog.dismiss()
                callback.onNegativeButtonClicked()
            }
            .show()
}

fun Context.showPermissionRequestDialog(message: Int) {
    showConfirmationDialog(object : AlertDialogCallback {
        override fun onPositiveButtonClicked() {
            val i = Intent()
            i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.data = Uri.parse("package:$packageName")
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            startActivity(i)
        }

        override fun onNegativeButtonClicked() {
        }
    }, getString(message), negativeButton = "Not Now")
}

fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Context.showToast(message: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun TextView.setFirstLetterCapitalisedText(string: String) {
    val capitalisedText = Character.toUpperCase(string[0]) + string.substring(1)
    text = capitalisedText
}

private fun capitalize(line: String): String {
    return Character.toUpperCase(line[0]) + line.substring(1)
}

fun BigDecimal.roundUpTo2DecimalPlaces(): BigDecimal {
    return this.setScale(2, BigDecimal.ROUND_HALF_UP)
}

interface AlertDialogCallback {
    fun onPositiveButtonClicked()
    fun onNegativeButtonClicked()
}

