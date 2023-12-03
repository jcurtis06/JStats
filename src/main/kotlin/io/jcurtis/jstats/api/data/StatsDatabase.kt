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

            // Create Players Table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS Players (" +
                        "player_uuid TEXT PRIMARY KEY," +
                        "player_name TEXT" +
                        ")"
            )

            // Create Statistics Table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS Statistics (" +
                        "stat_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "stat_name TEXT NOT NULL" +
                        ")"
            )

            // Create PlayerStats Table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS PlayerStats (" +
                        "player_uuid TEXT NOT NULL," +
                        "stat_id INTEGER NOT NULL," +
                        "stat_value TEXT NOT NULL," +
                        "PRIMARY KEY (player_uuid, stat_id)," +
                        "FOREIGN KEY (player_uuid) REFERENCES Players(player_uuid)," +
                        "FOREIGN KEY (stat_id) REFERENCES Statistics(stat_id)" +
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
            val statIdStatement = connection.prepareStatement(
                "SELECT stat_id FROM Statistics WHERE stat_name = ?"
            )
            statIdStatement.setString(1, stat.name)
            val statIdResult = statIdStatement.executeQuery()
            val statId = if (statIdResult.next()) statIdResult.getInt("stat_id") else -1
            statIdStatement.close()

            val updateStatement = connection.prepareStatement(
                "INSERT OR REPLACE INTO PlayerStats (player_uuid, stat_id, stat_value) VALUES (?, ?, ?)"
            )
            updateStatement.setString(1, playerId.toString())
            updateStatement.setInt(2, statId)
            updateStatement.setString(3, stat.getValueFor(playerId).serialize().toString())
            updateStatement.executeUpdate()
            updateStatement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun getStatValueFor(stat: Stat, playerId: UUID): StatValue<*>? {
        try {
            val statement = connection.prepareStatement(
                "SELECT ps.stat_value " +
                        "FROM PlayerStats ps " +
                        "INNER JOIN Statistics s ON ps.stat_id = s.stat_id " +
                        "WHERE s.stat_name = ? AND ps.player_uuid = ?"
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

            return if (value != null) stat.getType().deserialize(value) else null
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
    }

    fun addPlayer(playerId: UUID, playerName: String) {
        try {
            val statement = connection.prepareStatement(
                "INSERT OR IGNORE INTO Players (player_uuid, player_name) VALUES (?, ?)"
            )
            statement.setString(1, playerId.toString())
            statement.setString(2, playerName)
            statement.executeUpdate()
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
