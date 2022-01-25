package com.github.pocketkid2.deathswap.commands;

import org.bukkit.entity.Player;

import com.github.pocketkid2.deathswap.DeathSwapPlugin;

public class DeathSwapSettingsSubCommand extends DeathSwapSubCommand {

	public DeathSwapSettingsSubCommand(DeathSwapPlugin p) {
		super(p);
	}

	@Override
	public boolean isAdminCommand() {
		return true;
	}

	@Override
	public String getName() {
		return "settings";
	}

	@Override
	public void execute(Player player) {

	}

}
