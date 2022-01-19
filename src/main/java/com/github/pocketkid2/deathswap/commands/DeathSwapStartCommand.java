package com.github.pocketkid2.deathswap.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.pocketkid2.deathswap.DeathSwapGame.Status;
import com.github.pocketkid2.deathswap.DeathSwapPlugin;

public class DeathSwapStartCommand extends DeathSwapSubCommand {

	public DeathSwapStartCommand(DeathSwapPlugin p) {
		super(p);
	}

	@Override
	public boolean isAdminCommand() {
		return false;
	}

	@Override
	public String getName() {
		return "start";
	}

	@Override
	public void execute(Player player) {
		if (plugin.getGame().isPlayer(player) && plugin.getGame().getStatus().equals(Status.WAITING)) {
			if (plugin.canStart()) {
				plugin.getGame().playerVote(player);
				plugin.getGame().broadcastExcept(player,
						ChatColor.AQUA + "Player " + player.getDisplayName() + " has voted to start the game! (" + plugin.getGame().getVotes() + "/" + plugin.getGame().getPlayers().size() + ")");
				player.sendMessage(ChatColor.AQUA + "You have voted to start the game! (" + plugin.getGame().getVotes() + "/" + plugin.getGame().getPlayers().size() + ")");
				plugin.getGame().processVote();
			} else {
				player.sendMessage(ChatColor.RED + "The plugin has not been fully set up yet, please talk to an admin");
			}
		} else {
			player.sendMessage(ChatColor.RED + "You can only vote to start the game if you're in a game that's waiting to start!");
		}
	}

}
