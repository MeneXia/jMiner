package menexia.jminer;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class jMinerListener implements Listener 
{
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event)
	{
		int block = event.getBlock().getTypeId();
		if(jMiner.config.contains("Blocks." + block))
		{
			int moneyEarned = jMiner.config.getInt("Blocks." + block + ".");
			if(moneyEarned != 0)
			{
				EconomyResponse paid = jMiner.economy.depositPlayer(event.getPlayer().getName(), moneyEarned);
				if(paid.transactionSuccess())
				{
					if(jMiner.f.contains(event.getPlayer()))
					{
						event.getPlayer().sendMessage(jMiner.onBreak.replaceAll("%earned%", String.valueOf(moneyEarned)));
					}
				}
				else
				{
					event.getPlayer().sendMessage(ChatColor.RED + "ERROR: Report this to the developer MeneXia.");
				}
			}
		}
	}
}
