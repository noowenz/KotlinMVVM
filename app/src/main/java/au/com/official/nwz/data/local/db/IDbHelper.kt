package au.com.official.nwz.data.local.db

import au.com.official.nwz.data.model.db.Competitions
import au.com.official.nwz.data.model.db.Person
import io.reactivex.Observable

/**
 * Created by Nabin Shrestha on 5/30/2018.
 */
interface IDbHelper {
    fun getAllPeople(): Observable<MutableList<Person>>
    fun insertPeople(person: Person): Observable<Long>
    fun deleteByUserId(uid: Long): Observable<Int>

    fun getAllCompetitions(): Observable<MutableList<Competitions>>
    fun insertCompetitions(competitions: List<Competitions>): Observable<Long>
}