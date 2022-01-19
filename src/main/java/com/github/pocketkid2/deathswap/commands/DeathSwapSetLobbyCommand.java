package com.github.pocketkid2.deathswap.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.pocketkid2.deathswap.DeathSwapPlugin;

public class DeathSwapSetLobbyCommand extends DeathSwapSubCommand {

	public DeathSwapSetLobbyCommand(DeathSwapPlugin p) {
		super(p);
	}

	@Override
	public boolean isAdminCommand() {
		return true;
	}

	@Override
	public String getName() {
		return "setlobby";
	}

	@Override
	public void execute(Player player) {
		plugin.setLobby(player.getLocation());
		player.sendMessage(ChatColor.AQUA + "Set lobby to your location!");
	}

}
