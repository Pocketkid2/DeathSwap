package com.github.pocketkid2.deathswap.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.pocketkid2.deathswap.DeathSwapPlugin;

public class DeathSwapJoinCommand extends DeathSwapSubCommand {

	public DeathSwapJoinCommand(DeathSwapPlugin p) {
		super(p);
	}

	@Override
	public boolean isAdminCommand() {
		return false;
	}

	@Override
	public String getName() {
		return "join";
	}

	@Override
	public void execute(Player player) {
		switch (plugin.getGame().getStatus()) {
		case IN_GAME:
			player.sendMessage(plugin.addPrefix(ChatColor.RED + "The game is already in progress, you can't join now!"));
			break;
		case STARTING:
		case WAITING:
			if (plugin.getGame().addPlayer(player)) {
				player.sendMessage(ChatColor.AQUA + "You have joined the game!");
				plugin.getGame().broadcast(ChatColor.AQUA + player.getDisplayName() + " has joined the game (" + ChatColor.GREEN + plugin.getGame().getPlayers().size() + ChatColor.AQUA + ")");
			} else {
				player.sendMessage(plugin.addPrefix(ChatColor.RED + "You are already in the game!"));
			}
			break;
		}
	}
}
