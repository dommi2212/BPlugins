package me.dommi2212.BPlugins;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

public class BPlugins extends Plugin {
	
	public static Plugin instance = null;
	
	public static TextComponent noperm = new TextComponent("You don't have the permission to use this command!");
	public static TextComponent nopermargs = new TextComponent("You don't have the permission to use this (sub)command with this arguments!");
	
	@Override
	public void onEnable() {
		instance = this;
		
		BungeeCord.getInstance().getPluginManager().registerCommand(this, new CommandBPlugins("bplugins"));
		
		noperm.setColor(ChatColor.RED);
		nopermargs.setColor(ChatColor.RED);
	}
	
	@Override
	public void onDisable() {
		instance = null;
	}
	
}
