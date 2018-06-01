package au.com.official.nwz.utils.bindings

import android.databinding.BindingAdapter
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import au.com.official.nwz.R
import au.com.official.nwz.utils.FORMAT_DAY_MONTH_YEAR
import au.com.official.nwz.utils.FORMAT_MONTH_YEAR
import au.com.official.nwz.utils.convertDateToString
import au.com.official.nwz.utils.extensions.setFirstLetterCapitalisedText
import java.util.*

object BindingAdapters {
    @BindingAdapter("enableButton")
    @JvmStatic
    fun enableButtonEvent(button: Button, enableButton: Boolean) {
        with(button) {
            isEnabled = enableButton
        }
    }
}
