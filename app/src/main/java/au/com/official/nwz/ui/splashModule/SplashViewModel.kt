package au.com.official.nwz.ui.splashModule

import android.content.res.Resources
import au.com.official.nwz.base.viewModel.BaseViewModel
import au.com.official.nwz.data.DataManager
import javax.inject.Inject

class SplashViewModel
@Inject constructor(
        resources: Resources,
        val dataManager: DataManager) : BaseViewModel(resources) {
    fun handleNavigation() {

    }
}