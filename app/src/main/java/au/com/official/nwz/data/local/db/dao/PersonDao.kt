package au.com.official.nwz.data.local.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import au.com.official.nwz.data.model.db.Person

@Dao
interface PersonDao {

  @Query("SELECT * FROM person")
  fun getAllPeople(): MutableList<Person>

  @Insert
  fun insert(person: Person): Long

  @Query("DELETE FROM person WHERE uid = :userId")
  fun deleteByUserId(userId: Long): Int
}