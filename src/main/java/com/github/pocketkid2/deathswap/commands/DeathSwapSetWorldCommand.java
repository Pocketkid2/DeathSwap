package com.github.pocketkid2.deathswap.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.pocketkid2.deathswap.DeathSwapPlugin;

public class DeathSwapSetWorldCommand extends DeathSwapSubCommand {

	public DeathSwapSetWorldCommand(DeathSwapPlugin p) {
		super(p);
	}

	@Override
	public boolean isAdminCommand() {
		return true;
	}

	@Override
	public String getName() {
		return "setworld";
	}

	@Override
	public void execute(Player player) {
		plugin.setWorld(player.getWorld());
		player.sendMessage(ChatColor.AQUA + "Set world to your current world!");
	}

}
