package com.github.pocketkid2.deathswap.commands;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.pocketkid2.deathswap.DeathSwapGame.Status;
import com.github.pocketkid2.deathswap.DeathSwapPlugin;

import net.md_5.bungee.api.ChatColor;

public class DeathSwapSpectateCommand extends DeathSwapSubCommand {

	public DeathSwapSpectateCommand(DeathSwapPlugin p) {
		super(p);
	}

	@Override
	public boolean isAdminCommand() {
		return false;
	}

	@Override
	public String getName() {
		return "spectate";
	}

	@Override
	public void execute(Player player) {
		if (plugin.getGame().isPlayer(player)) {
			player.sendMessage(ChatColor.RED + "You can't spectate if you're already in the game!");
			return;
		}

		if (plugin.getGame().getStatus() != Status.IN_GAME) {
			player.sendMessage(ChatColor.RED + "The game is not currently in progress!");
			return;
		}

		player.teleport(plugin.getGame().getPlayers().get(0).getLocation());
		new BukkitRunnable() {

			@Override
			public void run() {
				player.setGameMode(GameMode.SPECTATOR);
			}
		}.runTaskLater(plugin, 20);

		player.sendMessage(ChatColor.AQUA + "You are now spectating the game!");
		plugin.getGame().addSpectator(player);
	}

}
