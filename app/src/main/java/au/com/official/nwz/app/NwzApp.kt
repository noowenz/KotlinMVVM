package au.com.official.nwz.app

import android.app.Activity
import android.app.Application
import au.com.official.nwz.R
import au.com.official.nwz.utils.extensions.initDagger
import com.crashlytics.android.Crashlytics
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import javax.inject.Inject

class NwzApp : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        initDagger()
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector
}
