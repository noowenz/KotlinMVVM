package au.com.official.nwz.data.local.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import au.com.official.nwz.data.local.db.dao.PersonDao
import au.com.official.nwz.data.model.db.Competitions
import au.com.official.nwz.data.model.db.Person

@Database(entities = arrayOf(Person::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun personDao(): PersonDao
}