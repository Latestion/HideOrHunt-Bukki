package me.latestion.hoh.myrunnables;

import me.latestion.hoh.data.flat.FlatHOHGame;
import me.latestion.hoh.game.HOHGame;
import me.latestion.hoh.localization.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import me.latestion.hoh.HideOrHunt;

import java.io.File;

public class Episodes extends BukkitRunnable {

	private HideOrHunt plugin;

	public Episodes(HideOrHunt plugin) {
		this.plugin = plugin;
		runTaskTimer(plugin, (plugin.getConfig().getInt("Episode-Time") * 60 * 20),
				(plugin.getConfig().getInt("Episode-Time") * 60 * 20L));
		sendReminders();
	}

	@Override
	public void run() {
		HOHGame game = plugin.getGame();
		MessageManager messageManager = plugin.getMessageManager();
		Bukkit.broadcastMessage(messageManager.getMessage("episode-end").replace("%episode%", Integer.toString(game.ep)));
		if (game.ep == 1) {
			if (game.grace)
				game.graceOff();
		}
		int breakTime = plugin.getConfig().getInt("Episode-End-Break-Time");
		if (breakTime != 0) {
			game.freezeGame();
			Bukkit.broadcastMessage(messageManager.getMessage("game-freezed"));
			if(breakTime != -1) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						game.unFreezeGame();
						Bukkit.broadcastMessage(messageManager.getMessage("game-unfreezed"));
						sendReminders();
					}
				}, (plugin.getConfig().getInt("Episode-End-Break-Time") * 20));
			}
		} else {
			sendReminders();
		}
		game.ep++;
		FlatHOHGame.save(game, plugin, new File(plugin.getDataFolder(), "hohGame.yml"));
	}

	private void sendReminders() {
		for (String s : plugin.getConfig().getStringList("Episode-Reminders")) {
			String[] split = s.split(", ");
			int interval = Integer.parseInt(split[0]);
			String message = format(split[1].replace("%ep%", Integer.toString(plugin.game.ep)));
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				public void run() {
					Bukkit.broadcastMessage(message);
				}
			}, interval * 20L);
		}
	}

	private String format(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
