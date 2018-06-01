package au.com.official.nwz.base.activity

import android.app.ProgressDialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import au.com.official.nwz.R

abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var dataBinding: V
    private lateinit var progressDialog: ProgressDialog
    private var toolbar: Toolbar? = null

    @LayoutRes
    abstract fun getLayout(): Int

    abstract fun initBinder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        progressDialog = ProgressDialog(this)
        initBinder()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    fun showProgressDialog(message: Int = 0) {
        var messageText = getString(R.string.msg_loading)
        if (message != 0) {
            messageText = getString(message)
        }
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setMessage(messageText)
        progressDialog.show()
    }

    /**
     * Function called by activities to setup toolbar with title and back button
     * @param toolbar toolbar from view
     * @param title title of the toolbar
     * @param backButtonEnabled true if back button is required else false
     */
    protected fun setupToolbar(toolbar: Toolbar, title: Any = R.string.app_name, backButtonEnabled: Boolean = true) {
        this.toolbar = toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(backButtonEnabled)
            findViewById<TextView>(R.id.tv_toolbar_title).apply {
                when (title) {
                    is Int -> setText(title)
                    is String -> text = title
                }
            }
        }
    }

    /**
     * Called by fragments to customise toolbar
     * @param title toolbar title
     * @param backButtonEnabled true if back button is required else false
     *
     */
    fun customiseToolbar(title: Int, backButtonEnabled: Boolean = true, notificationCount: Int = 0) {
        toolbar?.let {
            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
                setDisplayHomeAsUpEnabled(backButtonEnabled)
                findViewById<TextView>(R.id.tv_toolbar_title).apply {
                    setText(title)
                }
                if (notificationCount != 0) {
                    findViewById<TextView>(R.id.tv_toolbar_notifications_count).apply {
                        visibility = View.VISIBLE
                        text = notificationCount.toString()
                    }
                } else {
                    findViewById<TextView>(R.id.tv_toolbar_notifications_count).apply {
                        visibility = View.GONE
                        text = null
                    }
                }
            }
        }
    }

    fun hideProgressDialog() {
        progressDialog.cancel()
    }

    private fun performDataBinding() {
        dataBinding = DataBindingUtil.setContentView(this, getLayout())
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
