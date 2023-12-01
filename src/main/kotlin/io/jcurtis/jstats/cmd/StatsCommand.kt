package io.jcurtis.jstats.cmd

import io.jcurtis.jstats.JStats
import org.bukkit.Statistic
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StatsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            val stats = JStats.instance.registry.getStats()
            sender.sendMessage("Your stats:")
            for (stat in stats) {
                sender.sendMessage("${stat.name}: ${stat.getValueFor(sender.uniqueId).serialize()}")
            }

            for (stat in Statistic.entries) {
                if (!stat.isSubstatistic) {
                    sender.sendMessage("${stat.name}: ${sender.getStatistic(stat)}")
                } else {
                    sender.sendMessage("sub-stat")
                }
            }
        }
        return true
    }
}
