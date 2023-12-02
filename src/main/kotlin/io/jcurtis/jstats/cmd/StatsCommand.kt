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
                val statName = args[0]
                val stat = registry.getStat(statName)

                if (stat == null) {
                    sender.sendMessage("Stat $statName not found")
                    return true
                }

                val playerName = if (args.size > 1) args[1] else sender.name
                val player = sender.server.getPlayer(playerName)
                if (player != null) {
                    val statValue = stat.getValueFor(player.uniqueId).serialize()
                    sender.sendMessage("Player $playerName has $statValue $statName")
                } else {
                    sender.sendMessage("Player $playerName not found")
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
