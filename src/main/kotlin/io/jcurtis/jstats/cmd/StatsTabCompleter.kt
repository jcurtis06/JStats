package io.jcurtis.jstats.cmd

import io.jcurtis.jstats.JStats
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter


class StatsTabCompleter : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): MutableList<String> {
        val completions = mutableListOf<String>()

        if (args == null) return mutableListOf()

        val registry = JStats.instance.registry

        when (args.size) {
            1 -> {
                completions.addAll(registry.getProviders())
            }
            2 -> {
                val selectedPlugin = args[0]

                completions.addAll(registry.getStats().filter { it.category[0] == selectedPlugin }.map {
                    if (it.category.size > 1) {
                        it.category[1]
                    } else {
                        it.name
                    }
                })
            }
            3 -> {
                val selectedPlugin = args[0]
                val selectedCategory = args[1]

                completions.addAll(registry.getStats().filter {
                    if (it.category.size > 1) {
                        it.category[0] == selectedPlugin && it.category[1] == selectedCategory
                    } else {
                        it.category[0] == selectedPlugin && it.name == selectedCategory
                    }
                }.map { it.name })
            }
        }

        return completions
    }
}