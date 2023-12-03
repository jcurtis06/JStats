package io.jcurtis.jstats.cmd

import io.jcurtis.jstats.JStats
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StatsCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is Player) {
            val registry = JStats.instance.registry

            if (args != null && args.isNotEmpty()) {
                val statId = args.joinToString(":")
                val stat = registry.getStat(statId)

                if (stat != null) {
                    val statValue = stat.getValueFor(sender.uniqueId).serialize()
                    sender.sendMessage("You have $statValue ${stat.name}")
                } else {
                    sender.sendMessage("Stat not found $statId")
                }
            } else {
                for (stat in registry.getStats()) {
                    val statValue = stat.getValueFor(sender.uniqueId).serialize()
                    sender.sendMessage("You have $statValue ${stat.name}")
                }
            }

        }
        return true
    }
}
