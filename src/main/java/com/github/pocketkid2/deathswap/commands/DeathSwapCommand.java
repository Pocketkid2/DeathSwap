package com.github.pocketkid2.deathswap.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.pocketkid2.deathswap.DeathSwapPlugin;

public class DeathSwapCommand implements CommandExecutor {

	private DeathSwapPlugin plugin;
	private List<DeathSwapSubCommand> subs;

	public DeathSwapCommand(DeathSwapPlugin p) {
		plugin = p;
		subs = new ArrayList<DeathSwapSubCommand>();
		subs.add(new DeathSwapJoinCommand(plugin));
		subs.add(new DeathSwapQuitCommand(plugin));
		subs.add(new DeathSwapStartCommand(plugin));
		subs.add(new DeathSwapSetLobbyCommand(plugin));
		subs.add(new DeathSwapSetWorldCommand(plugin));
		subs.add(new DeathSwapSpectateCommand(plugin));
		subs.add(new DeathSwapListCommand(plugin));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player player) {
			if (args.length > 0) {
				if (args.length < 2) {
					for (DeathSwapSubCommand sub : subs) {
						if (sub.getName().equalsIgnoreCase(args[0])) {
							if ((sub.isAdminCommand() && player.isOp()) || !sub.isAdminCommand()) {
								sub.execute(player);
							} else {
								player.sendMessage(ChatColor.RED + "You don't have permission to use that command!");
							}
						}
					}
				} else {
					player.sendMessage(ChatColor.RED + "Too many arguments!");
					help(player, label);
				}
			} else {
				player.sendMessage(ChatColor.RED + "Not enough arguments!");
				help(player, label);
			}
		} else {
			sender.sendMessage("You must be a player!");
		}
		return true;
	}

	private void help(Player player, String label) {
		player.sendMessage(
				ChatColor.AQUA + "Showing commands for " + ChatColor.GOLD + plugin.getDescription().getName() + ChatColor.AQUA + " version " + ChatColor.BLUE + plugin.getDescription().getVersion());
		for (DeathSwapSubCommand sub : subs) {
			if ((sub.isAdminCommand() && player.isOp()) || !sub.isAdminCommand()) {
				player.sendMessage(ChatColor.GRAY + "/" + label + " " + sub.getName());
			}
		}
	}

}
