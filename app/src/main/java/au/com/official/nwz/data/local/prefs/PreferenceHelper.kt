package au.com.official.nwz.data.local.prefs

import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceHelper
@Inject constructor(
        context: Context,
        val gson: Gson) : IPreferenceHelper {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        const val IS_LOGGED_IN = "is_logged_in"
        const val TOKEN = "token"
        const val USER = "user"
        const val FIRST_NAME = "first_name"
    }

    override fun setBearerToken(token: String): Completable {
        setKeyValues(TOKEN, "Bearer $token")
        return Completable.complete()
    }

    override fun getBearerToken(): String {
        return getStringValues(TOKEN)
    }

    override fun setPersonName(personName: String): Completable {
        setKeyValues(FIRST_NAME, personName)
        return Completable.complete()
    }

    override fun getPersonName(): String = getStringValues(FIRST_NAME)

    override fun setLoggedIn(boolean: Boolean): Completable {
        setKeyValues(IS_LOGGED_IN, boolean)
        return Completable.complete()
    }

    override fun isLoggedIn(): Boolean = getBoolValues(IS_LOGGED_IN)


    private fun setKeyValues(key: String, value: String?) {
        prefs.edit().putString(key, value).apply()
    }

    private fun setKeyValues(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    private fun setKeyValues(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    private fun setKeyValues(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    private fun getStringValues(key: String): String {
        return prefs.getString(key, "")
    }

    private fun getIntValues(key: String): Int {
        val nullValue = 0
        return prefs.getInt(key, nullValue)
    }

    private fun getLongValues(key: String): Long {
        val nullValue: Long = 0
        return prefs.getLong(key, nullValue)
    }

    private fun getBoolValues(key: String): Boolean {
        return prefs.getBoolean(key, false)
    }
}