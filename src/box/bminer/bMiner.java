package box.bminer;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class bMiner extends JavaPlugin
{
	public static Economy economy = null;
	
	public static FileConfiguration config;
	
	Logger log = Logger.getLogger("Minecraft");

	@Override
	public void onDisable() 
	{
		log.info("[bMiner] {" + getDescription().getVersion() + "} has been disabled!");
	}

	@Override
	public void onEnable() 
	{
		createConfiguration();
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvents(new bMinerListener(), this);
		setupEconomy();
		log.info("[bMiner] {" + getDescription().getVersion() + "} has been enabled!");
	}
	
	// Making a configuration file.
	public void createConfiguration()
	{
		config = getConfig();
		config.addDefault("Send-Player-Message-On-Block-Break", false);
		config.addDefault("Blocks.1", 29);
		config.options().copyDefaults(true);
		saveConfig();
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
