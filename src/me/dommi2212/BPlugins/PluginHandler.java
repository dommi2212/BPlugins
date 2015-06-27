package me.dommi2212.BPlugins;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.api.plugin.PluginManager;

public class PluginHandler {
	
	private static HashMap<String, Boolean> enabledMap = new HashMap<String, Boolean>();
	
	private PluginHandler() {}
	
	public static boolean loadPlugin(Plugin plugin) {
		Map<PluginDescription, Boolean> pluginStatuses = new HashMap<PluginDescription, Boolean>();
		PluginDescription description = plugin.getDescription();
		boolean success = false;
		try {
			Method method = PluginManager.class.getDeclaredMethod("enablePlugin", Map.class, Stack.class, PluginDescription.class);
			method.setAccessible(true);
			success = (boolean) method.invoke(BungeeCord.getInstance().getPluginManager(), pluginStatuses, new Stack<PluginDescription>(), description);	  
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {		
			e.printStackTrace();
		}
		
		if(!success) {
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "Failed to enable " + plugin.getDescription().getName());
		}
		return success;
	}

	public static boolean enablePlugin(Plugin plugin) {
		boolean success = false;
		try {
			plugin.onEnable();
			ProxyServer.getInstance().getLogger().log(Level.INFO, "Enabled " + plugin.getDescription().getName());
			success = true;
		} catch (Throwable t) {
			ProxyServer.getInstance().getLogger().log(Level.WARNING, "Failed to load plugin " + plugin.getDescription().getName(), t);
		}
		return success;
	}
	
	public static void disablePlugin(Plugin plugin) {
		plugin.onDisable();
		BungeeCord.getInstance().getPluginManager().unregisterCommands(plugin);
		BungeeCord.getInstance().getPluginManager().unregisterListeners(plugin);
	}
	
	public static boolean isEnabled(String name) {
		if(enabledMap.containsKey(name)) {			
			return enabledMap.get(name);
		} else return false;
	}
	
	public static void changeStatus(String name, boolean toChange) {
		enabledMap.remove(name);
		enabledMap.put(name, toChange);
	}

	protected static void loadAllPlugins() {
		if(enabledMap.isEmpty()) {
			for(Plugin plugin : BungeeCord.getInstance().getPluginManager().getPlugins()) enabledMap.put(plugin.getDescription().getName(), true);
		}
	}

}
