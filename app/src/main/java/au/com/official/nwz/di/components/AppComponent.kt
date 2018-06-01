package au.com.official.nwz.di.components

import android.app.Application
import au.com.official.nwz.app.NwzApp
import au.com.official.nwz.di.contributor.ActivityContributor
import au.com.official.nwz.di.modules.AppModule
import au.com.official.nwz.di.modules.NetModule
import au.com.official.nwz.di.modules.StorageModule
import au.com.official.nwz.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityContributor::class,
    AppModule::class,
    NetModule::class,
    StorageModule::class,
    ViewModelModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: NwzApp)
}
