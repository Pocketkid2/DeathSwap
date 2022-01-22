package com.github.pocketkid2.deathswap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.md_5.bungee.api.ChatColor;

public class DeathSwapListener implements Listener {

	private List<Player> respawns;

	private DeathSwapPlugin plugin;

	public DeathSwapListener(DeathSwapPlugin p) {
		plugin = p;
		respawns = new ArrayList<Player>();
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity();
			if (plugin.getGame().isPlayer(player) && ((player.getHealth() - event.getDamage()) <= 0)) {
				plugin.broadcast(player.getDisplayName() + ChatColor.AQUA + " died while playing Death Swap (" + ChatColor.GOLD + event.getCause().toString() + ChatColor.AQUA + ")");
				plugin.getGame().broadcast(player.getDisplayName() + ChatColor.AQUA + " died (" + ChatColor.GOLD + event.getCause().toString() + ChatColor.AQUA + ")");
				if (plugin.getGame().getPlayers().size() > 1) {
					plugin.getGame().broadcast(ChatColor.GREEN + "" + (plugin.getGame().getPlayers().size() - 1) + ChatColor.AQUA + " players remaining!");
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (plugin.getGame().isPlayer(event.getEntity())) {
			plugin.getLogger().info("Logging death swap death for player " + event.getEntity());
			event.setDeathMessage(null);
			event.getEntity().getInventory().clear();
			respawns.add(event.getEntity());
			plugin.getGame().removePlayer(event.getEntity());
			// plugin.broadcast(event.getEntity().getDisplayName() + " died while playing
			// Death Swap!");
			if (plugin.getGame().getPlayers().size() < 2) {
				// plugin.getGame().broadcast(event.getEntity().getDisplayName() + " died");
				plugin.getGame().processPlayerRemoved();
			} else {
				// plugin.getGame().broadcast(event.getEntity().getDisplayName() + " died, " +
				// plugin.getGame().getPlayers().size() + " players remaining!");
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

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (plugin.getGame().isSpectator(event.getPlayer()) && (event.getAction() == Action.LEFT_CLICK_AIR)) {
			Random random = new Random();
			plugin.getGame().getPlayers().get(random.nextInt(plugin.getGame().getPlayers().size()));
		}
	}
}
