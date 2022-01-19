package com.github.pocketkid2.deathswap;

import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.pocketkid2.deathswap.commands.DeathSwapCommand;

public class DeathSwapPlugin extends JavaPlugin {

	private DeathSwapGame game;
	private World world;
	private int radius;
	private int startingTimeSecs;
	private int swapTimeSecs;
	private Location lobby;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		// Load the world
		if (getConfig().getString("world") == null) {
			getLogger().severe("No world name specified! Plugin will not work until set correctly");
		} else {
			world = getServer().getWorld(getConfig().getString("world"));
			if (world == null) {
				getLogger().severe("World name is invalid! Plugin will not work until set correctly");
			} else {
				getLogger().info("World loaded as " + world.getName());
			}
		}

		// Load the radius
		radius = getConfig().getInt("radius");
		getLogger().info("Radius loaded as " + radius);

		// Load the times
		startingTimeSecs = getConfig().getInt("starting-time");
		getLogger().info("Starting time loaded as " + startingTimeSecs + " seconds");
		swapTimeSecs = getConfig().getInt("swap-time");
		getLogger().info("Swap time loaded as " + swapTimeSecs + " seconds");

		// Load the lobby location
		lobby = getConfig().getLocation("lobby-spawn", null);
		if (lobby == null) {
			getLogger().severe("No lobby specified! Plugin will not work until set correctly");
		} else {
			getLogger().info("Lobby loaded as " + lobby.toString());
		}

		// Initialize the game instance
		game = new DeathSwapGame(this);

		// Set up the command
		getCommand("deathswap").setExecutor(new DeathSwapCommand(this));

		// Set up the listener
		getServer().getPluginManager().registerEvents(new DeathSwapListener(this), this);

		// We're done, notify the console
		getLogger().info("Enabled!");
	}

	@Override
	public void onDisable() {
		// Any changes made, save to disk
		saveConfig();

		// We're done, notify the console
		getLogger().info("Disabled!");
	}

	public DeathSwapGame getGame() {
		return game;
	}

	public int getRadius() {
		return radius;
	}

	// Broadcasts the given message to players not in the game
	public void broadcast(String s) {
		for (Player p : getServer().getOnlinePlayers().stream().collect(Collectors.toList())) {
			if (!game.isPlayer(p)) {
				p.sendMessage(s);
			}
		}
	}

	public boolean canStart() {
		return (world != null) && (lobby != null);
	}

	public World getWorld() {
		return world;
	}

	public Location getLobby() {
		return lobby;
	}

	public void setLobby(Location location) {
		lobby = location;
	}

	public void setWorld(World w) {
		world = w;
	}
}
