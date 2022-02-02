package com.github.pocketkid2.deathswap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class DeathSwapChunkListener implements Listener {

	private DeathSwapPlugin plugin;

	private Map<Player, List<Chunk>> players;

	public DeathSwapChunkListener(DeathSwapPlugin p) {
		plugin = p;
		players = new HashMap<>();
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.getTo().getChunk() != event.getFrom().getChunk()) {
			Player player = event.getPlayer();
			if (plugin.getGame().isPlayer(player)) {
				Chunk current = event.getTo().getChunk();

				// Create a list of chunks to look at
				List<Chunk> chunks;
				if (players.containsKey(player)) {
					chunks = players.get(player);
				} else {
					chunks = new ArrayList<Chunk>();
				}

				// Clear chunks whose distance is 2 or greater
				for (Chunk c : chunks) {
					if (chunkDistance(c, current) >= 2) {
						chunks.remove(c);
						c.setForceLoaded(false);
					}
				}

				// Look for new chunks to load
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						if ((j != 0) || (i != 0)) {
							Chunk c = current.getWorld().getChunkAt(current.getX() + i, current.getZ() + j);
							if (!chunks.contains(c)) {
								c.setForceLoaded(true);
								chunks.add(c);
							}
						}
					}
				}
			}
		}
	}

	// Returns linear chunk distance between two chunks
	public int chunkDistance(Chunk a, Chunk b) {
		int diffX = Math.abs(a.getX() - b.getX());
		int diffZ = Math.abs(a.getZ() - b.getZ());
		return diffX > diffZ ? diffX : diffZ;
	}
}
