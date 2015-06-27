package me.dommi2212.BPlugins;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class CommandBPlugins extends Command {
	
	public CommandBPlugins(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender cs, String[] args) {
		if(!cs.hasPermission("bplugins.use")) {
			cs.sendMessage(BPlugins.noperm);
			return;
		}
		PluginManager pm = BungeeCord.getInstance().getPluginManager();
		PluginHandler.loadAllPlugins();
		if(args.length == 0) {
			if(!cs.hasPermission("bplugins.plugins")) {
				cs.sendMessage(BPlugins.nopermargs);
				return;
			}
			String plstring = "§7Plugins: (§6" + pm.getPlugins().size() + "§7) ";
			for(Plugin plugin : pm.getPlugins()) {
				String name = plugin.getDescription().getName();
				if(name.equals("reconnect_yaml") || name.equals("cmd_find") || name.equals("cmd_server")
						|| name.equals("cmd_alert") || name.equals("cmd_send") || name.equals("cmd_list")) {
					if(PluginHandler.isEnabled(plugin.getDescription().getName())) plstring = plstring + ", §a" + name;
					else plstring = plstring + ", §c" + name;
				} else {
					if(PluginHandler.isEnabled(plugin.getDescription().getName())) plstring = plstring + ", §2" + name;
					else plstring = plstring + ", §4" + name;
				}
			}
			TextComponent component = new TextComponent(plstring.replaceFirst(", ", "") + "\n§7Use §o\"/BPlugins help\"§7 for help!");
			cs.sendMessage(component);
		} else if(args.length == 1) {
			if(!cs.hasPermission("bplugins.help")) {
				cs.sendMessage(BPlugins.nopermargs);
				return;
			}
			if(args[0].equalsIgnoreCase("help")) {
				TextComponent message = new TextComponent("§7BPlugins-Help: (V. " + BPlugins.instance.getDescription().getVersion() + ") (Main-Permission: bplugins.use)\n"
						+ "§7Commands:\n");
				
				TextComponent plugins = new TextComponent("§6/BPlugins\n");
				plugins.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new TextComponent[]{new TextComponent("§6/BPlugins \n§7Permission: bplugins.plugins\n \n§7Shows all installes Plugins!\n§7Internal: §aEnabled §cDisabled\n§7External: §2Enabled §4Disabled")}));
				TextComponent help = new TextComponent("§6/BPlugins help\n");
				help.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new TextComponent[]{new TextComponent("§6/BPlugins help\n§7Permission: bplugins.help\n \n§7Shows the help!")}));
				TextComponent reload = new TextComponent("§6/BPlugins reload <Plugin>\n");
				reload.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new TextComponent[]{new TextComponent("§6/BPlugins reload <Plugin>\n§7Permission: bplugins.reload\n \n§7Reloads a plugin!")}));
				TextComponent disable = new TextComponent("§6/BPlugins disable <Plugin>\n");
				disable.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new TextComponent[]{new TextComponent("§6/BPlugins disable <Plugin>\n§7Permission: bplugins.disable\n \n§7Disables a plugin!")}));
				TextComponent enable = new TextComponent("§6/BPlugins enable <Plugin>\n");
				enable.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new TextComponent[]{new TextComponent("§6/BPlugins enable <Plugin>\n§7Permission: bplugins.enable\n \n§7Enables a plugin!")}));
				
				message.addExtra(plugins);
				message.addExtra(help);
				message.addExtra(reload);
				message.addExtra(disable);
				message.addExtra(enable);
				message.addExtra(new TextComponent("§7Hover commands for additional information!\n"));
				
				TextComponent notice = new TextComponent("§4This plugin is still in BETA! Report bugs §nhere!");
				notice.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new TextComponent[]{new TextComponent("§7Click here to start a conversation on Spigotmc.org with me!")}));
				notice.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, "http://www.spigotmc.org/conversations/add?to=dommi2212"));			
				message.addExtra(notice);
				
				cs.sendMessage(message);
			} else if(args[0].equalsIgnoreCase("reload")) {
				cs.sendMessage(new TextComponent("§cSyntax: /BPlugins reload <Plugin>"));
			} else if(args[0].equalsIgnoreCase("disable")) {
				cs.sendMessage(new TextComponent("§cSyntax: /BPlugins disable <Plugin>"));
			} else if(args[0].equalsIgnoreCase("enable")) {
				cs.sendMessage(new TextComponent("§cSyntax: /BPlugins enable <Plugin>"));
			} else {
				cs.sendMessage(new TextComponent("§cUnknown subcommand \"" + args[0] + "\"! Use §o\"/BPlugins help\"§c for help!"));
			}
		} else if(args.length == 2) {
			if(args[0].equalsIgnoreCase("reload")) {
				if(!cs.hasPermission("bplugins.reload")) {
					cs.sendMessage(BPlugins.nopermargs);
					return;
				}
				Plugin plugin = pm.getPlugin(args[1]);
				if(plugin == null) {
					cs.sendMessage(new TextComponent("§cUnknown Plugin!"));
					return;
				}
				cs.sendMessage(new TextComponent("§aReloading plugin " + args[1]));
				plugin.onDisable();
				pm.unregisterCommands(plugin);
				pm.unregisterListeners(plugin);
				boolean successload = PluginHandler.loadPlugin(plugin);
				boolean successenable = PluginHandler.enablePlugin(plugin);
				if(successload && successenable) cs.sendMessage(new TextComponent("§aDone!"));
				else cs.sendMessage(new TextComponent("§cAn error occurred! See console for additional information!"));	
			} else if(args[0].equalsIgnoreCase("disable")) {
				if(!cs.hasPermission("bplugins.disable")) {
					cs.sendMessage(BPlugins.nopermargs);
					return;
				}
				Plugin plugin = pm.getPlugin(args[1]);
				if(plugin == null) {
					cs.sendMessage(new TextComponent("§cUnknown Plugin!"));
					return;
				}
				if(!PluginHandler.isEnabled(plugin.getDescription().getName())) {
					cs.sendMessage(new TextComponent("§cPlugin already disabled!"));
					return;
				}
				cs.sendMessage(new TextComponent("§aDisabling plugin " + args[1]));
				PluginHandler.disablePlugin(plugin);
				PluginHandler.changeStatus(plugin.getDescription().getName(), false);
				cs.sendMessage(new TextComponent("§aDone!"));
			} else if(args[0].equalsIgnoreCase("enable")) {
				if(!cs.hasPermission("bplugins.enable")) {
					cs.sendMessage(BPlugins.nopermargs);
					return;
				}
				Plugin plugin = pm.getPlugin(args[1]);
				if(plugin == null) {
					cs.sendMessage(new TextComponent("§cUnknown Plugin!"));
					return;
				}
				if(PluginHandler.isEnabled(plugin.getDescription().getName())) {
					cs.sendMessage(new TextComponent("§cPlugin already enabled!"));
					return;
				}
				cs.sendMessage(new TextComponent("§aEnabling plugin " + args[1]));
				boolean successload = PluginHandler.loadPlugin(plugin);
				boolean successenable = PluginHandler.enablePlugin(plugin);
				if(successload && successenable) {
					PluginHandler.changeStatus(plugin.getDescription().getName(), true);
					cs.sendMessage(new TextComponent("§aDone!"));
				}
				else cs.sendMessage(new TextComponent("§cAn error occurred! See console for additional information!"));
			} else if(args[0].equalsIgnoreCase("help")) {
				cs.sendMessage(new TextComponent("§cSyntax: /BPlugins help"));
			} else {
				cs.sendMessage(new TextComponent("§cUnknown subcommand \"" + args[0] + "\"! Use §o\"/BPlugins help\"§c for help!"));
			}
		} else {
			cs.sendMessage(new TextComponent("§cSyntax Error! Use §o\"/BPlugins help\"§c for help!"));
		}
	}
}
