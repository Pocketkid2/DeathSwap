package com.github.pocketkid2.deathswap;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import com.github.pocketkid2.deathswap.DeathSwapTimer.Job;

public class DeathSwapGame {

	private DeathSwapPlugin plugin;

	public enum Status {
		WAITING, STARTING, IN_GAME;
	}

	private List<Player> players;
	private Status status;
	private List<Player> votes;
	private BukkitTask task;
	private List<Player> spectators;

	public DeathSwapGame(DeathSwapPlugin p) {
		plugin = p;
		players = new ArrayList<Player>();
		status = Status.WAITING;
		task = null;
		votes = new ArrayList<Player>();
		spectators = new ArrayList<Player>();
	}

	public void cancelTask() {
		if (task != null) {
			task.cancel();
		}
	}

	public void setTask(BukkitTask t) {
		if ((task != null) && !task.isCancelled()) {
			task.cancel();
		}
		task = t;
	}

	public void broadcast(String message) {
		for (Player p : players) {
			p.sendMessage(plugin.addPrefix(message));
		}
	}

	public void broadcastExcept(Player player, String message) {
		for (Player p : players) {
			if (p != player) {
				p.sendMessage(plugin.addPrefix(message));
			}
		}
	}

	public List<Player> getPlayers() {
		return players;
	}

	public boolean addPlayer(Player p) {
		if (players.contains(p))
			return false;
		players.add(p);
		return true;
	}

	public boolean removePlayer(Player p) {
		if (!players.contains(p))
			return false;
		players.remove(p);
		return true;
	}

	public boolean isPlayer(Player p) {
		return players.contains(p);
	}

	public void clearPlayers() {
		players.clear();
	}

	public Status getStatus() {
		return status;
	}

	public int getVotes() {
		return votes.size();
	}

	public boolean playerVote(Player player) {
		if (votes.contains(player))
			return false;
		votes.add(player);
		return true;
	}

	public void clearVotes() {
		votes.clear();
	}

	public void processVote() {
		if (players.size() >= 2) {
			if (votes.size() == players.size()) {
				broadcast(ChatColor.AQUA + "All players are ready, game now starting!");
				status = Status.STARTING;
				plugin.broadcast(ChatColor.AQUA + "The Death Swap game is starting!");
				new DeathSwapTimer(plugin, plugin.getStartingTimeSecs(), ChatColor.AQUA + "The game will start in " + ChatColor.DARK_AQUA + "%d" + ChatColor.AQUA + " %s", Job.START_GAME)
						.runTaskTimer(plugin, 20, 20);
				clearVotes();
			}
		} else {
			players.get(0).sendMessage(plugin.addPrefix(ChatColor.RED + "The game cannot start until there are at least two players!"));
		}
	}

	public void processPlayerRemoved() {
		if (players.size() < 2) {
			Player winner = players.get(0);
			winner.sendMessage(plugin.addPrefix(ChatColor.GREEN + "You won Death Swap!"));
			plugin.broadcast(ChatColor.GREEN + winner.getDisplayName() + ChatColor.GREEN + " won Death Swap!");
			winner.teleport(plugin.getLobby());
			winner.getInventory().clear();
			clearPlayers();
			cancelTask();
			status = Status.WAITING;
			for (Player p : spectators) {
				p.teleport(plugin.getLobby());
				p.sendMessage(plugin.addPrefix(ChatColor.GREEN + "The Death Swap game is over, " + winner.getDisplayName() + " won the game!"));
			}
			spectators.clear();
		}
	}

	public void forceStop() {
		broadcast(ChatColor.GREEN + "The game is being force stopped");
		for (Player p : players) {
			p.teleport(plugin.getLobby());
			p.getInventory().clear();
			p.setHealth(20);
			p.setFoodLevel(20);
			p.setExp(0);
		}
		cancelTask();
		status = Status.WAITING;
		for (Player p : spectators) {
			p.teleport(plugin.getLobby());
			p.sendMessage(plugin.addPrefix(ChatColor.GREEN + "The Death Swap game has been force stopped"));
		}
		clearPlayers();
		spectators.clear();
	}

	public void setStatus(Status s) {
		status = s;
	}

	public void addSpectator(Player player) {
		spectators.add(player);
	}

	public boolean isSpectator(Player player) {
		return spectators.contains(player);
	}
}
