package me.latestion.hoh.events;

import me.latestion.hoh.game.HOHGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import me.latestion.hoh.HideOrHunt;
import me.latestion.hoh.game.GameState;

public class GameModeChange implements Listener {

	private HideOrHunt plugin;

	public GameModeChange(final HideOrHunt plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void c(PlayerGameModeChangeEvent event) {
		if (GameState.getCurrentGameState() != GameState.ON) return;
		if (!plugin.game.hohPlayers.containsKey(event.getPlayer().getUniqueId())) return;
		if (!this.plugin.getConfig().getBoolean("Allow-Op")) return;
		if (!event.getNewGameMode().equals(GameMode.SPECTATOR)) {
			Bukkit.broadcastMessage(event.getPlayer().getName() + ChatColor.BOLD + "" + ChatColor.RED + " Just went in " + event.getNewGameMode().toString().toUpperCase());
		}
	}
}
