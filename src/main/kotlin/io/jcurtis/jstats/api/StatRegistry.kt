package io.jcurtis.jstats.api

import io.jcurtis.jstats.api.cache.DataCache
import io.jcurtis.jstats.api.data.StatsDatabase
import io.jcurtis.jstats.api.stat.Stat

class StatRegistry(private val database: StatsDatabase) {
    private val stats = mutableListOf<Stat>()

    fun registerStat(stat: Stat) {
        stat.database = database
        stat.dataCache = DataCache(database, stat)
        stats.add(stat)
        println("Registered stat: ${stat.name}")
    }

    fun unregisterStat(stat: Stat) {
        stats.remove(stat)
        println("Unregistered stat: ${stat.name}")
    }

    fun getStats(): List<Stat> {
        return stats
    }

    fun getStat(name: String): Stat? {
        return stats.find { it.name == name }
    }

    fun cleanup() {
        for (stat in stats) {
            stat.dataCache.clearOlderThan(System.currentTimeMillis())
        }
    }
}
