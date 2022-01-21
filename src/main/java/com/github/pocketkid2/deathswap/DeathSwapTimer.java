package com.github.pocketkid2.deathswap;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.pocketkid2.deathswap.DeathSwapGame.Status;

public class DeathSwapTimer extends BukkitRunnable {

	public enum Job {
		START_GAME, SWAP;
	}

	private DeathSwapPlugin plugin;
	private int secondsLeft;
	private String message;
	private Job job;

	public DeathSwapTimer(DeathSwapPlugin p, int seconds, String m, Job j) {
		plugin = p;
		job = j;
		secondsLeft = seconds;
		message = m;
	}

	@Override
	public void run() {
		secondsLeft--;
		plugin.getLogger().info("Running timer task for job " + job.toString() + ", s = " + secondsLeft);
		if (secondsLeft > 0) {
			int min = secondsLeft / 60;
			int sec = secondsLeft % 60;
			if ((min > 0) && (sec == 0)) {
				plugin.getGame().broadcast(String.format(message, min, "minute" + ((min > 1) ? "s" : "")));
			} else if ((min <= 0) && (sec <= 10)) {
				plugin.getGame().broadcast(String.format(message, sec, "second" + ((sec > 1) ? "s" : "")));
			}
		} else {
			switch (job) {
			case START_GAME:
				plugin.getGame().setStatus(Status.IN_GAME);
				plugin.getGame().broadcast("Starting game, " + (plugin.getFirstSwapSecs() / 60) + " minutes until first swap!");
				plugin.broadcast("Death Swap game is starting");
				for (Player p : plugin.getGame().getPlayers()) {
					p.teleport(getRandomLocationInWorld(plugin.getWorld()));
					p.setHealth(20);
					p.setFoodLevel(20);
					p.setExp(0);
				}
				plugin.getGame().setTask(new DeathSwapTimer(plugin, plugin.getFirstSwapSecs(), "Swapping players in %s %s", Job.SWAP).runTaskTimer(plugin, 20, 20));
				cancel();
				break;
			case SWAP:
				plugin.getGame().broadcast("Swapping players!");
				List<Location> locs = plugin.getGame().getPlayers().stream().map(Player::getLocation).collect(Collectors.toList());
				Location first = locs.get(0);
				locs.remove(0);
				locs.add(first);
				for (int i = 0; i < plugin.getGame().getPlayers().size(); i++) {
					plugin.getGame().getPlayers().get(i).teleport(locs.get(i));
				}
				plugin.getGame().setTask(new DeathSwapTimer(plugin, plugin.getSwapTimeSecs(), "Swapping players in %s %s", Job.SWAP).runTaskTimer(plugin, 20, 20));
				cancel();
				break;
			default:
				break;

			}
		}
	}

	public Location getRandomLocationInWorld(World world) {
		Random random = new Random();
		int randX = random.nextInt(plugin.getRadius() * 2) - plugin.getRadius();
		int randZ = random.nextInt(plugin.getRadius() * 2) - plugin.getRadius();
		int y = world.getHighestBlockYAt(randX, randZ) + 1;
		return new Location(world, randX, y, randZ);
	}

}
