package io.jcurtis.jstats.api.cache

import io.jcurtis.jstats.api.data.StatsDatabase
import io.jcurtis.jstats.api.stat.Stat
import java.util.*

class DataCache(
    private val database: StatsDatabase,
    private val stat: Stat
) {
    val cache = mutableMapOf<UUID, DataCacheEntry>()

    fun clearOlderThan(timestamp: Long) {
        for (entry in cache.entries) {
            if (entry.value.timestamp < timestamp) {
                database.updateStatForPlayer(stat, entry.key)
                cache.remove(entry.key)
                println("Removed entry for ${entry.key}")
            }
        }
    }

    fun getEntryFor(uuid: UUID): DataCacheEntry? {
        var entry: DataCacheEntry?

        if (cache.containsKey(uuid)) {
            println("Found entry for $uuid")
            entry = cache[uuid]!!
            entry.timestamp = System.currentTimeMillis()
            return entry
        }

        load(uuid)?.let {
            println("Loaded entry for $uuid")
            entry = it
            cache[uuid] = it
            return entry
        }

        println("No entry found for $uuid")

        return null
    }

    private fun load(uuid: UUID): DataCacheEntry? {
        return database.getStatValueFor(stat, uuid)?.let {
            DataCacheEntry(
                it,
                System.currentTimeMillis()
            )
        }
    }
}
