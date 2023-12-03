package io.jcurtis.jstats.event

import io.jcurtis.jstats.api.data.StatsDatabase
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinEvent(private val database: StatsDatabase): Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        database.addPlayer(event.player.uniqueId, event.player.name)
    }
}