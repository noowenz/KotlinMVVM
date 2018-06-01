package au.com.official.nwz.data

import au.com.official.nwz.base.model.BaseResponse
import au.com.official.nwz.data.local.db.AppDbHelper
import au.com.official.nwz.data.local.prefs.PreferenceHelper
import au.com.official.nwz.data.model.db.Competitions
import au.com.official.nwz.data.rest.IApiService
import au.com.official.nwz.data.model.db.Person
import au.com.official.nwz.data.model.rest.Landing
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Nabin Shrestha on 5/28/2018.
 */

@Singleton
class DataManager @Inject
constructor(
        private val apiService: IApiService,
        private val appDbHelper: AppDbHelper,
        private val preferenceHelper: PreferenceHelper): IDataManager{

    override fun getAllCompetitions(): Observable<MutableList<Competitions>> {
        return appDbHelper.getAllCompetitions()
    }

    override fun insertCompetitions(competitionsList: List<Competitions>): Observable<Long> {
        return appDbHelper.insertCompetitions(competitionsList)
    }

    override fun getCompetitions(): Observable<List<Competitions>> {
        return apiService.getCompetitions()
    }

    override fun deleteByUserId(uid: Long): Observable<Int> {
        return appDbHelper.deleteByUserId(uid)
    }

    override fun insertPeople(person: Person): Observable<Long> {
        return appDbHelper.insertPeople(person)
    }

    override fun getAllPeople(): Observable<MutableList<Person>> {
        return appDbHelper.getAllPeople()
    }

    override fun setLoggedIn(boolean: Boolean): Completable {
        return preferenceHelper.setLoggedIn(boolean)
    }

    override fun isLoggedIn(): Boolean {
        return preferenceHelper.isLoggedIn()
    }

    override fun registerUser(request: Landing): Observable<BaseResponse<Landing>> {
        return apiService.registerUser(request)
    }

    override fun setBearerToken(token: String): Completable {
        return preferenceHelper.setBearerToken(token)
    }

    override fun getBearerToken(): String {
        return preferenceHelper.getBearerToken()
    }

    override fun setPersonName(personName: String): Completable {
        return preferenceHelper.setPersonName(personName)
    }

    override fun getPersonName(): String {
        return preferenceHelper.getPersonName()
    }
}