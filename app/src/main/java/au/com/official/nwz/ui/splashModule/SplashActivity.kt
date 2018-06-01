package au.com.official.nwz.ui.splashModule

import android.os.Bundle
import au.com.official.nwz.R
import au.com.official.nwz.base.activity.BaseActivity
import au.com.official.nwz.data.DataManager
import au.com.official.nwz.databinding.ActivitySplashBinding
import au.com.official.nwz.di.injector.Injectable
import au.com.official.nwz.ui.landing.LandingActivity
import au.com.official.nwz.utils.extensions.transitionFadeOut
import javax.inject.Inject


class SplashActivity : BaseActivity<ActivitySplashBinding>(), Injectable {
    @Inject
    lateinit var dataManager: DataManager

    override fun initBinder() {
    }

    override fun getLayout(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (dataManager.isLoggedIn()) {
        } else {
            LandingActivity.start(this)
            finish()
            transitionFadeOut()
        }
    }
}
