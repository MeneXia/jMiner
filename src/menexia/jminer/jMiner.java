package menexia.jminer;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class jMiner extends JavaPlugin
{
	public static Economy economy = null;
	
	public static FileConfiguration config;
	
	Logger logger = Logger.getLogger("Minecraft");
	
	public static String tag = "&6jMiner";
	public static String onBreak = "%tag% &4You have just added &b%earned% &4to your currency";
	
	public static Set<Player> f = new HashSet<Player>();
	public static jMinerCommand cmdHandler; 

	@Override
	public void onDisable() 
	{
		f.clear();
		status("disabled");
	}

	@Override
	public void onEnable() 
	{
		createConfiguration();
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new jMinerListener(), this);
		getCommand("jminer").setExecutor(cmdHandler);
		setupEconomy();
		status("enabled");
	}
	
	public void status(String status) {
		PluginDescriptionFile pdf = this.getDescription();
		this.logger.info("[jMiner] version " + pdf.getVersion() + " by MeneXia is now " + status + "!");
	}
	
	public static String colorize(String message) {
		return message.replaceAll("&([a-f0-9])", ChatColor.COLOR_CHAR + "$1");
	}
	
	public void loadVariables() {
		tag = colorize(this.getConfig().getString("plugin-tag"));
		onBreak = colorize(this.getConfig().getString("message-on-break")).replace("%tag%", tag);
	}
	
	// Making a configuration file.
	public void createConfiguration()
	{
		try {
			File fileconfig = new File(getDataFolder(), "config.yml");
			if (!fileconfig.exists()) {
				getDataFolder().mkdir();
				config = this.getConfig();
				config.addDefault("plugin-tag", "&6[jMiner]");
				config.addDefault("message-on-break", "%tag% &4You have just added &b%earned% &4to your currency!");
				config.addDefault("Blocks.1", 29);
				config.options().copyDefaults(true);
				
				config.options().header("jMiner config file\n"
						+ "plugin-tag is the suffix of in-game messages"
						+ "message-on-break is what you receive when breaking a specified block in the list below"
						+ "Also supports color codes: &a-f, &0-9"
						+ "Use Block IDs | Material: Money (e.g. 3: 15) - One earns 15 from breaking dirt (3)");
				config.options().copyHeader(true);
				
				saveConfig();
			}
		} catch (Exception e1){
			e1.printStackTrace();
		}
		
	}
	
    private Boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}
