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
    private val type: Statistic.Type = Statistic.Type.UNTYPED
) : Stat(name, IntStat(0)) {
    override fun getValueFor(playerId: UUID): StatValue<*> {
        val player = Bukkit.getOfflinePlayer(playerId)

        when (type) {
            Statistic.Type.BLOCK -> {
                val block = Material.getMaterial(name.split(":")[0]) ?: return IntStat(0)

                return IntStat(player.getStatistic(Statistic.valueOf(name.uppercase()), block))
            }
            Statistic.Type.ITEM -> {
                val item = Material.getMaterial(name.split(":")[0]) ?: return IntStat(0)

                return IntStat(player.getStatistic(Statistic.valueOf(name.uppercase()), item))
            }
            Statistic.Type.ENTITY -> {
                println("Entity: " + name.split(":")[1])
                println("Name: " + name.uppercase())
                val entity = EntityType.valueOf(name.split(":")[1].uppercase())
                if (entity == EntityType.UNKNOWN) {
                    return IntStat(0)
                }
                return IntStat(player.getStatistic(Statistic.valueOf(name.uppercase().split(":")[0]), entity))
            }
            Statistic.Type.UNTYPED -> {
                return IntStat(player.getStatistic(Statistic.valueOf(name.uppercase())))
            }
        }
    }

    override fun setValueFor(playerId: UUID, value: StatValue<*>) {
        val player = Bukkit.getOfflinePlayer(playerId)

        when (type) {
            Statistic.Type.BLOCK -> {
                val block = Material.getMaterial(name.split(":")[0]) ?: return

                player.setStatistic(Statistic.valueOf(name.uppercase()), block, value.value as Int)
            }
            Statistic.Type.ITEM -> {
                val item = Material.getMaterial(name.split(":")[0]) ?: return

                player.setStatistic(Statistic.valueOf(name.uppercase()), item, value.value as Int)
            }
            Statistic.Type.ENTITY -> {
                val entity = EntityType.valueOf(name.split(":")[0].uppercase())

                player.setStatistic(Statistic.valueOf(name.uppercase()), entity, value.value as Int)
            }
            Statistic.Type.UNTYPED -> {
                player.setStatistic(Statistic.valueOf(name.uppercase()), value.value as Int)
            }
        }
    }
}