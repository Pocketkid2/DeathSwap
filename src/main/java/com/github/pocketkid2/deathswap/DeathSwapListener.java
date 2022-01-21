package com.github.pocketkid2.deathswap;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathSwapListener implements Listener {

	private List<Player> respawns;

	private DeathSwapPlugin plugin;

	public DeathSwapListener(DeathSwapPlugin p) {
		plugin = p;
		respawns = new ArrayList<Player>();
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (plugin.getGame().isPlayer(event.getEntity())) {
			plugin.getLogger().info("Logging death swap death for player " + event.getEntity());
			event.setDeathMessage(null);
			event.getEntity().getInventory().clear();
			respawns.add(event.getEntity());
			plugin.getGame().removePlayer(event.getEntity());
			plugin.broadcast(event.getEntity().getDisplayName() + " died while playing Death Swap!");
			if (plugin.getGame().getPlayers().size() < 2) {
				plugin.getGame().broadcast(event.getEntity().getDisplayName() + " died");
				plugin.getGame().processPlayerRemoved();
			} else {
				plugin.getGame().broadcast(event.getEntity().getDisplayName() + " died, " + plugin.getGame().getPlayers().size() + " players remaining!");
			}
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (plugin.getGame().isPlayer(event.getPlayer())) {
			plugin.getLogger().info("Logging player quit event for player " + event.getPlayer());
			event.setQuitMessage(null);
			plugin.getGame().removePlayer(event.getPlayer());
			plugin.broadcast(event.getPlayer().getDisplayName() + " left the server while playing Death Swap!");
			if (plugin.getGame().getPlayers().size() < 2) {
				plugin.getGame().broadcast(event.getPlayer().getDisplayName() + " left the server");
				plugin.getGame().processPlayerRemoved();
			} else {
				plugin.getGame().broadcast(event.getPlayer().getDisplayName() + " left the server, " + plugin.getGame().getPlayers().size() + " players remaining!");
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (respawns.contains(event.getPlayer())) {
			plugin.getLogger().info("Logging player respawn event for player " + event.getPlayer());
			event.setRespawnLocation(plugin.getLobby());
			respawns.remove(event.getPlayer());
		}
	}
}
