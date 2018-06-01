package au.com.official.nwz.utils.extensions

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import au.com.official.nwz.app.NwzApp
import au.com.official.nwz.base.activity.BaseActivity
import au.com.official.nwz.di.components.DaggerAppComponent
import au.com.official.nwz.di.injector.Injectable
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection

/**
 * Helper class to automatically inject activities and fragments if they implement [Injectable]
 */
fun NwzApp.initDagger() {
    DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

    registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {
        }

        override fun onActivityResumed(activity: Activity?) {
        }

        override fun onActivityStarted(activity: Activity?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            handleActivity(activity)
        }
    })
}

private fun handleActivity(activity: Activity?) {
    activity?.let {
        if (activity is BaseActivity<*> && activity is Injectable) {
            AndroidInjection.inject(activity)

            (activity as BaseActivity<*>).supportFragmentManager
                    .registerFragmentLifecycleCallbacks(
                            object : FragmentManager.FragmentLifecycleCallbacks() {
                                override fun onFragmentCreated(fm: FragmentManager?, f: Fragment?,
                                                               savedInstanceState: Bundle?) {
                                    if (f is Injectable) {
                                        AndroidSupportInjection.inject(f)
                                    }
                                }
                            }, true)
        }
    }
}