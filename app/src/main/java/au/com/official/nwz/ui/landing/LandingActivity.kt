package au.com.official.nwz.ui.landing

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import au.com.official.nwz.R
import au.com.official.nwz.base.activity.BaseActivity
import au.com.official.nwz.base.viewModel.ViewModelFactory
import au.com.official.nwz.databinding.ActivityLandingBinding
import au.com.official.nwz.di.injector.Injectable
import au.com.official.nwz.utils.extensions.*
import kotlinx.android.synthetic.main.activity_landing.*
import javax.inject.Inject


class LandingActivity : BaseActivity<ActivityLandingBinding>(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    companion object {
        fun start(context: Context) {
            val intent = Intent()
            intent.setClass(context, LandingActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): Int = R.layout.activity_landing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI(activity_landing)
        initViews()
    }

    override fun initBinder() {
        dataBinding.viewModel = obtainViewModel(LandingViewModel::class.java, viewModelFactory).apply {
            fetchCompetitionsFromServer()
            getFromDB()
            alertMessageEvent.observe(this@LandingActivity, Observer {
                it?.let {
                    showAlert(it)
                }
            })
            progressDialogEvent.observe(this@LandingActivity, Observer {
                it?.let {
                    if (it != -1) {
                        showProgressDialog(it)
                    } else {
                        hideProgressDialog()
                    }
                }
            })
        }
    }

    private fun initViews() {
        setupToolbar(dataBinding.toolbar as Toolbar, title = R.string.title_landing)

        dataBinding.rvPerson.apply {
            val layoutManager = LinearLayoutManager(this@LandingActivity)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            this.layoutManager = layoutManager
            adapter = dataBinding.viewModel?.let {
                LandingAdapter(it)
            }
            val mDividerItemDecoration = DividerItemDecoration(
                    this@LandingActivity,
                    layoutManager.orientation
            )
            addItemDecoration(mDividerItemDecoration)
        }

    }
}
