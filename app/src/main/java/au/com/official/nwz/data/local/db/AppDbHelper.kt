package au.com.official.nwz.data.local.db

import au.com.official.nwz.data.model.db.Competitions
import au.com.official.nwz.data.model.db.Person
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Nabin Shrestha on 5/30/2018.
 */
@Singleton
class AppDbHelper @Inject
constructor(val database: AppDatabase): IDbHelper {
    override fun getAllCompetitions(): Observable<MutableList<Competitions>> {
        return Observable.fromCallable<MutableList<Competitions>> { database.competitionsDao().getAllCompetitions() }
    }

    override fun insertCompetitions(competitions: List<Competitions>): Observable<Long> {
        return Observable.fromCallable<Long> {
            database.competitionsDao().insertCompetitions(competitions)
        }
    }

    override fun deleteByUserId(uid: Long): Observable<Int> {
        return Observable.fromCallable<Int> {
            database.personDao().deleteByUserId(uid)
        }
    }

    override fun insertPeople(person: Person): Observable<Long> {
        return Observable.fromCallable<Long> {
            database.personDao().insert(person)
        }
    }

    override fun getAllPeople(): Observable<MutableList<Person>> {
        return Observable.fromCallable<MutableList<Person>> { database.personDao().getAllPeople() }
    }

}