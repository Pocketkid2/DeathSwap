package com.github.pocketkid2.deathswap.commands;

import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import com.github.pocketkid2.deathswap.DeathSwapPlugin;

import net.md_5.bungee.api.ChatColor;

public class DeathSwapListCommand extends DeathSwapSubCommand {

	public DeathSwapListCommand(DeathSwapPlugin p) {
		super(p);
	}

	@Override
	public boolean isAdminCommand() {
		return false;
	}

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public void execute(Player player) {
		player.sendMessage(plugin.addPrefix("Game status: " + ChatColor.BLUE + plugin.getGame().getStatus().toString()));
		player.sendMessage(plugin.addPrefix(ChatColor.AQUA + "Players (" + ChatColor.GREEN + plugin.getGame().getPlayers().size() + ChatColor.AQUA + "): "
				+ String.join(ChatColor.AQUA + ", ", plugin.getGame().getPlayers().stream().map(Player::getDisplayName).collect(Collectors.toList()))));
	}

}
