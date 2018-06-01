package au.com.official.nwz.data

import au.com.official.nwz.data.local.db.IDbHelper
import au.com.official.nwz.data.local.prefs.IPreferenceHelper
import au.com.official.nwz.data.rest.IApiService

interface IDataManager: IApiService, IDbHelper, IPreferenceHelper {

    enum class LoggedInMode constructor(val type: Int) {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_GOOGLE(1),
        LOGGED_IN_MODE_FB(2),
        LOGGED_IN_MODE_SERVER(3)
    }
}

