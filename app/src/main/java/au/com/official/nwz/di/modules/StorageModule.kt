package au.com.official.nwz.di.modules

import au.com.official.nwz.data.local.prefs.IPreferenceHelper
import au.com.official.nwz.data.local.prefs.PreferenceHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {
    @Provides
    @Singleton
    fun providePreferenceManager(preferenceHelper: PreferenceHelper): IPreferenceHelper = preferenceHelper
}