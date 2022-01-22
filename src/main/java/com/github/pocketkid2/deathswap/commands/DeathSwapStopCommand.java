package com.github.pocketkid2.deathswap.commands;

import org.bukkit.entity.Player;

import com.github.pocketkid2.deathswap.DeathSwapPlugin;

import net.md_5.bungee.api.ChatColor;

public class DeathSwapStopCommand extends DeathSwapSubCommand {

	public DeathSwapStopCommand(DeathSwapPlugin p) {
		super(p);
	}

	@Override
	public boolean isAdminCommand() {
		return true;
	}

	@Override
	public String getName() {
		return "stop";
	}

	@Override
	public void execute(Player player) {
		plugin.getGame().forceStop();
		player.sendMessage(plugin.addPrefix(ChatColor.AQUA + "You force stopped the game"));
	}

}
