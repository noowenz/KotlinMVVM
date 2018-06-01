package au.com.official.nwz.di.contributor

import au.com.official.nwz.ui.landing.LandingActivity
import au.com.official.nwz.ui.splashModule.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityContributor {
    @ContributesAndroidInjector()
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeLandingActivity(): LandingActivity
}