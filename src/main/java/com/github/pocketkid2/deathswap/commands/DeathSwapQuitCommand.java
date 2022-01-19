package com.github.pocketkid2.deathswap.commands;

import org.bukkit.entity.Player;

import com.github.pocketkid2.deathswap.DeathSwapGame.Status;
import com.github.pocketkid2.deathswap.DeathSwapPlugin;

import net.md_5.bungee.api.ChatColor;

public class DeathSwapQuitCommand extends DeathSwapSubCommand {

	public DeathSwapQuitCommand(DeathSwapPlugin p) {
		super(p);
	}

	@Override
	public boolean isAdminCommand() {
		return false;
	}

	@Override
	public String getName() {
		return "quit";
	}

	@Override
	public void execute(Player player) {
		if (plugin.getGame().isPlayer(player)) {
			plugin.getGame().removePlayer(player);
			player.sendMessage(ChatColor.AQUA + "You have left the game!");
			plugin.getGame().broadcast(ChatColor.AQUA + player.getDisplayName() + " has left the game (" + plugin.getGame().getPlayers().size() + ")");
			if (plugin.getGame().getStatus() == Status.IN_GAME) {
				player.teleport(plugin.getLobby());
				plugin.getGame().processPlayerRemoved();
			}
		} else {
			player.sendMessage(ChatColor.RED + "You are not in the game!");
		}
	}

}
