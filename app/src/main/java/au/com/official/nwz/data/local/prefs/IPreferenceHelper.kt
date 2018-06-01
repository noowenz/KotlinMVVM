package au.com.official.nwz.data.local.prefs

import io.reactivex.Completable
import io.reactivex.Observable

interface IPreferenceHelper {
    fun setBearerToken(token: String): Completable
    fun getBearerToken(): String
    fun setPersonName(personName: String): Completable
    fun getPersonName(): String
    fun setLoggedIn(boolean: Boolean): Completable
    fun isLoggedIn(): Boolean
}