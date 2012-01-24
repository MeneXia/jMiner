package bloan.plugin;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class bLoan extends JavaPlugin
{
	// Logger.
	Logger l = Logger.getLogger("Minecraft");
	
	// Vault Stuffs.
	 public static Economy economy = null;
	
	@Override
	public void onDisable() {
		l.info("[bLoan] {" + getDescription().getVersion() + "} has been disabled!");
	}

	@Override
	public void onEnable() {
		setupEconomy();
		if(this.getServer().getPluginManager().getPlugin("Vault").isEnabled())
		{
			getCommand("loan").setExecutor(new bLoanCmd());
		l.info("[bLoan] {" + getDescription().getVersion() + "} has been enabled!");
		}
		else
		{
			l.severe("[bLoan] Vault not found!");
			this.setEnabled(false);
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
