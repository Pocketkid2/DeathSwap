package com.github.pocketkid2.deathswap.commands;

import org.bukkit.entity.Player;

import com.github.pocketkid2.deathswap.DeathSwapPlugin;

public abstract class DeathSwapSubCommand {

	protected DeathSwapPlugin plugin;

	public DeathSwapSubCommand(DeathSwapPlugin p) {
		plugin = p;
	}

	public abstract boolean isAdminCommand();

	public abstract String getName();

	public abstract void execute(Player player);
}
