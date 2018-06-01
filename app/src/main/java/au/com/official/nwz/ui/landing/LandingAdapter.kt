package au.com.official.nwz.ui.landing

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import au.com.official.nwz.base.viewHolder.Binder
import au.com.official.nwz.data.model.db.Person
import au.com.official.nwz.databinding.ItemPersonBinding

class LandingAdapter
constructor(private val landingViewModel: LandingViewModel,
            private val peopleList: MutableList<Person> = arrayListOf())
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).onBind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = peopleList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(private val itemPersonBinding: ItemPersonBinding)
        : RecyclerView.ViewHolder(itemPersonBinding.root), Binder {
        override fun onBind(position: Int) {
            with(itemPersonBinding) {
                data = peopleList[position]
                root.setOnClickListener {
                    landingViewModel.deleteByUserId(data!!.uid)
                }

                executePendingBindings()
            }
        }
    }

    fun setPersonList(list: List<Person>?) {
        list?.let {
            peopleList.clear()
            peopleList.addAll(it)
            notifyDataSetChanged()
        }
    }
}
