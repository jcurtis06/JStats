package io.jcurtis.jstats.api.stat

import io.jcurtis.jstats.api.stat.value.IntStat
import io.jcurtis.jstats.api.stat.value.StatValue
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Statistic
import org.bukkit.entity.EntityType
import java.util.UUID

class VanillaStat(
    name: String,
    category: MutableList<String> = mutableListOf(),
    private val type: Statistic.Type = Statistic.Type.UNTYPED
) : Stat(name, IntStat(0), category) {
    override fun getValueFor(playerId: UUID): StatValue<*> {
        val player = Bukkit.getOfflinePlayer(playerId)

        when (type) {
            Statistic.Type.BLOCK -> {
                val block = Material.getMaterial(name.uppercase()) ?: return IntStat(0)

                return IntStat(player.getStatistic(Statistic.valueOf(category[1].uppercase()), block))
            }
            Statistic.Type.ITEM -> {
                val item = Material.getMaterial(name.uppercase()) ?: return IntStat(0)

                return IntStat(player.getStatistic(Statistic.valueOf(category[1].uppercase()), item))
            }
            Statistic.Type.ENTITY -> {
                val entity = EntityType.valueOf(name.uppercase())
                if (entity == EntityType.UNKNOWN) {
                    return IntStat(0)
                }
                return IntStat(player.getStatistic(Statistic.valueOf(category[1].uppercase()), entity))
            }
            Statistic.Type.UNTYPED -> {
                return IntStat(player.getStatistic(Statistic.valueOf(name.uppercase())))
            }
        }
    }
}