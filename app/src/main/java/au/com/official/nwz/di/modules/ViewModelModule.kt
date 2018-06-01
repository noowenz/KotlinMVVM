package au.com.official.nwz.di.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import au.com.official.nwz.base.viewModel.ViewModelFactory
import au.com.official.nwz.di.keys.ViewModelKey
import au.com.official.nwz.ui.landing.LandingViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LandingViewModel::class)
    abstract fun bindLandingViewModel(landingViewModel: LandingViewModel): ViewModel
}