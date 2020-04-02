package io.github.safyre.harvester;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Harvester extends JavaPlugin {

	static final ArrayList<Material> CROPS = new ArrayList<>();
	static final ArrayList<Material> TALL_CROPS = new ArrayList<>();
	static FileConfiguration config;
	private static Logger logger;
	private static final String PREFIX = "[Harvester] ";

	private static PluginManager pm = Bukkit.getPluginManager();

	@Override
	public void onEnable() {
		CROPS.addAll(Arrays.asList(
				Material.WHEAT,
				Material.BEETROOTS,
				Material.CARROTS,
				Material.POTATOES,
				Material.MELON,
				Material.PUMPKIN,
				Material.NETHER_WART,
				Material.COCOA
		));
		TALL_CROPS.addAll(Arrays.asList(
				Material.BAMBOO,
				Material.CACTUS,
				Material.SUGAR_CANE
		));
		saveDefaultConfig();
		config = getConfig();
		getServer().getPluginManager().registerEvents(new HarvestListener(), this);
		logger = getServer().getLogger();
	}

	public static void fireEvent(Event event){
		pm.callEvent(event);
	}

	public static void log(Level level, String message){
		logger.log(level, PREFIX + message);
	}

	public static void log(String message){
		logger.log(Level.INFO, PREFIX + message);
	}
}
