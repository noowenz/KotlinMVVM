package au.com.official.nwz.data.local.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import au.com.official.nwz.data.model.db.Competitions
import au.com.official.nwz.data.model.db.Person

/**
 * Created by Nabin Shrestha on 6/1/2018.
 */
@Dao
interface CompetitionsDao {
    @Insert
    fun insertCompetitions(competitions: List<Competitions>): Long

    @Query("SELECT * FROM competitions")
    fun getAllCompetitions(): MutableList<Competitions>
}