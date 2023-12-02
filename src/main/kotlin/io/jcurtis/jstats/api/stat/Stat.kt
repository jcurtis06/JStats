package io.jcurtis.jstats.api.stat

import io.jcurtis.jstats.api.cache.DataCache
import io.jcurtis.jstats.api.cache.DataCacheEntry
import io.jcurtis.jstats.api.data.StatsDatabase
import io.jcurtis.jstats.api.stat.value.StatValue
import org.bukkit.plugin.Plugin
import java.util.UUID

open class Stat(
    val name: String,
    private val defaultValue: StatValue<*>,
) {
    lateinit var database: StatsDatabase
    lateinit var dataCache: DataCache
    lateinit var id: String

    open fun getValueFor(playerId: UUID) : StatValue<*> {
        try {
            val entry = dataCache.getEntryFor(playerId) ?: return defaultValue
            entry.timestamp = System.currentTimeMillis()
            return entry.statValue
        } catch (e: UninitializedPropertyAccessException) {
            throw UninitializedPropertyAccessException("Stat $name has not been registered.")
        }
    }

    open fun setValueFor(playerId: UUID, value: StatValue<*>) {
        try {
            dataCache.cache[playerId]?.let {
                it.statValue = value
                it.timestamp = System.currentTimeMillis()
            } ?: run {
                dataCache.cache[playerId] = DataCacheEntry(value, System.currentTimeMillis())
            }
            println("Set value for stat: $name")
        } catch (e: UninitializedPropertyAccessException) {
            throw UninitializedPropertyAccessException("Stat $name has not been registered.")
        }
    }

    fun resetValueFor(playerId: UUID) {
        try {
            dataCache.cache.remove(playerId)
        } catch (e: UninitializedPropertyAccessException) {
            throw UninitializedPropertyAccessException("Stat $name has not been registered.")
        }
    }

    fun getCache() : DataCache {
        try {
            return dataCache
        } catch (e: UninitializedPropertyAccessException) {
            throw UninitializedPropertyAccessException("Stat $name has not been registered.")
        }
    }

    fun getType() : StatValue<*> {
        return defaultValue
    }
}
