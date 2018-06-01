package au.com.official.nwz.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import android.content.res.Resources
import android.provider.Settings
import au.com.official.nwz.R
import au.com.official.nwz.contracts.AppContract
import au.com.official.nwz.data.DataManager
import au.com.official.nwz.data.IDataManager
import au.com.official.nwz.data.local.db.AppDatabase
import au.com.official.nwz.data.local.db.AppDbHelper
import au.com.official.nwz.data.local.db.IDbHelper
import au.com.official.nwz.di.DatabaseInfo
import au.com.official.nwz.utils.rx.IScheduleProvider
import au.com.official.nwz.utils.rx.ScheduleProvider
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton


@Module
class AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    @Named(AppContract.DEVICE_ID)
    fun provideDeviceId(application: Application): String = Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID)

    @Provides
    @Singleton
    fun provideResources(application: Application): Resources = application.resources

    @Provides
    @Singleton
    fun provideScheduleProvider(scheduleProvider: ScheduleProvider): IScheduleProvider = scheduleProvider

    @Provides
    @DatabaseInfo
    internal fun provideDatabaseName(): String = AppContract.DB_NAME

    @Provides
    @Singleton
    fun provideDataManager(dataManager: DataManager): IDataManager = dataManager

    @Provides fun providesAppDatabase(@DatabaseInfo dbName: String, context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, dbName).allowMainThreadQueries().build()

    @Provides
    @Singleton
    internal fun provideDbHelper(appDbHelper: AppDbHelper): IDbHelper = appDbHelper

}