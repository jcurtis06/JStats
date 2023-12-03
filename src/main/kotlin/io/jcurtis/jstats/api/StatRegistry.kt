package io.jcurtis.jstats.api

import io.jcurtis.jstats.api.cache.DataCache
import io.jcurtis.jstats.api.data.StatsDatabase
import io.jcurtis.jstats.api.stat.Stat
import org.bukkit.plugin.Plugin

class StatRegistry(private val database: StatsDatabase) {
    private val stats = mutableListOf<Stat>()
    private val providers = mutableListOf<String>()

    fun registerStat(plugin: Plugin, stat: Stat) {
        stat.database = database
        stat.dataCache = DataCache(database, stat)
        stat.category.add(0, plugin.name.lowercase())
        stat.id = stat.category.joinToString(":") + ":" + stat.name.lowercase()
        stats.add(stat)
        providers.add(plugin.name.lowercase())
    }

    fun registerStat(plugin: Plugin, stat: Stat, category: String) {
        stat.database = database
        stat.dataCache = DataCache(database, stat)
        stat.id = plugin.name.lowercase() + ":" + category + ":" + stat.name.lowercase()
        stats.add(stat)
    }

    fun unregisterStat(stat: Stat) {
        stats.remove(stat)
    }

    fun getStats(): List<Stat> {
        return stats
    }

    fun getStat(id: String): Stat? {
        return stats.find { it.id == id }
    }

    fun getProviders(): List<String> {
        return providers
    }

    fun cleanup() {
        for (stat in stats) {
            stat.dataCache.clearOlderThan(System.currentTimeMillis())
        }
    }
}
