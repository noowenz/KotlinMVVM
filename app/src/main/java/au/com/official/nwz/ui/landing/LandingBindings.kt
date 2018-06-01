package au.com.official.nwz.ui.landing

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import au.com.official.nwz.data.model.db.Person

/**
 * Created by Nabin Shrestha on 5/31/2018.
 */
object LandingBindings {

    @BindingAdapter("listOfPerson")
    @JvmStatic
    fun setPersonList(recyclerView: RecyclerView, persons: List<Person>?) {
        with(recyclerView.adapter as LandingAdapter) {
            setPersonList(persons)
        }
    }

    @BindingAdapter("itemPerson")
    @JvmStatic
    fun setPersonName(textView: TextView, name: String?) {
        with(textView) {
            name?.let {
                text = it
            }
        }
    }
}