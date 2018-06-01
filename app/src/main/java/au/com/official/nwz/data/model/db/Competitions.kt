package au.com.official.nwz.data.model.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Nabin Shrestha on 6/1/2018.
 */
@Entity
data class Competitions(
        val _links: Links,
        @PrimaryKey
        val id: Int,
        val caption: String,
        val league: String,
        val year: String,
        val currentMatchday: Int,
        val numberOfMatchdays: Int,
        val numberOfTeams: Int,
        val numberOfGames: Int,
        val lastUpdated: String
) {
    data class Links(
            val self: Self,
            val teams: Teams,
            val fixtures: Fixtures,
            val leagueTable: LeagueTable
    ) {
        data class Self(
                val href: String
        )

        data class LeagueTable(
                val href: String
        )

        data class Teams(
                val href: String
        )

        data class Fixtures(
                val href: String
        )
    }
}