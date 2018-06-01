package au.com.official.nwz.base.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import au.com.official.nwz.base.activity.BaseActivity

abstract class BaseFragment<V : ViewDataBinding> : Fragment() {
    protected lateinit var baseActivity: BaseActivity<*>
    protected lateinit var dataBinding: V

    @LayoutRes
    abstract fun getLayout(): Int

    abstract fun initBinder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            baseActivity = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        performViewBinding(inflater, container)
        initBinder()
        return dataBinding.root
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> baseActivity.onBackPressed()
        }
        return true
    }

    private fun performViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        dataBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
    }
}