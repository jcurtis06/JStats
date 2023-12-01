package io.jcurtis.jstats.api.data

import io.jcurtis.jstats.api.stat.Stat
import io.jcurtis.jstats.api.stat.value.StatValue
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.UUID

class StatsDatabase(path: String) {
    private lateinit var connection: Connection

    init {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:$path")
            val statement = connection.createStatement()
            statement.execute(
                "CREATE TABLE IF NOT EXISTS stats (" +
                        "stat_name TEXT NOT NULL," +
                        "stat_value TEXT NOT NULL," +
                        "player_uuid TEXT NOT NULL," +
                        "PRIMARY KEY (stat_name, player_uuid)" +
                        ")"
            )
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun closeConnection() {
        try {
            if (!connection.isClosed) {
                connection.close()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun updateStatForPlayer(stat: Stat, playerId: UUID) {
        try {
            val statement = connection.prepareStatement(
                "INSERT OR REPLACE INTO stats (stat_name, stat_value, player_uuid) VALUES (?, ?, ?)"
            )
            statement.setString(1, stat.name)
            statement.setString(2, stat.getValueFor(playerId).serialize().toString())
            statement.setString(3, playerId.toString())
            statement.executeUpdate()
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun getStatValueFor(stat: Stat, playerId: UUID): StatValue<*>? {
        try {
            val statement = connection.prepareStatement(
                "SELECT stat_value FROM stats WHERE stat_name = ? AND player_uuid = ?"
            )
            statement.setString(1, stat.name)
            statement.setString(2, playerId.toString())
            val result = statement.executeQuery()
            val value = if (result.next()) {
                result.getString("stat_value")
            } else {
                null
            }
            statement.close()

            return stat.getType().deserialize(value ?: return null)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return null
    }
}
