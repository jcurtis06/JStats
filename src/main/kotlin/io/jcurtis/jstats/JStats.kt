package io.jcurtis.jstats

import io.jcurtis.jstats.api.StatRegistry
import io.jcurtis.jstats.api.data.StatsDatabase
import io.jcurtis.jstats.api.stat.Stat
import io.jcurtis.jstats.api.stat.value.IntStat
import io.jcurtis.jstats.cmd.StatsCommand
import org.bukkit.plugin.java.JavaPlugin

class JStats: JavaPlugin() {
    lateinit var registry: StatRegistry

    companion object {
        lateinit var instance: JStats
    }

    override fun onEnable() {
        saveDefaultConfig()
        instance = this
        registry = StatRegistry(StatsDatabase(dataFolder.absolutePath + "/stats.db"))

        getCommand("stats")?.setExecutor(StatsCommand())

        server.scheduler.scheduleSyncRepeatingTask(this, {
            registry.cleanup()
        }, 600, 600)

        val stat = Stat(
            "test-stat",
            IntStat(0),
        )

        registry.registerStat(stat)

        for (player in server.onlinePlayers) {
            stat.setValueFor(player.uniqueId, IntStat(1))
        }

        logger.info("JStats has been enabled!")
    }

    override fun onDisable() {

    }
}
