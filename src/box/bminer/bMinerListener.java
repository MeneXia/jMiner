package box.bminer;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class bMinerListener implements Listener 
{
	@EventHandler(event = BlockBreakEvent.class, priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event)
	{
		int block = event.getBlock().getTypeId();
		if(bMiner.config.contains("Blocks." + block))
		{
			int moneyEarned = bMiner.config.getInt("Blocks." + block + ".");
			if(moneyEarned != 0)
			{
				EconomyResponse paid = bMiner.economy.depositPlayer(event.getPlayer().getName(), moneyEarned);
				if(paid.transactionSuccess())
				{
					if(bMiner.config.getBoolean("Send-Player-Message-On-Block-Break") == true)
					{
						event.getPlayer().sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "bMiner" + ChatColor.GOLD + "] " + ChatColor.AQUA + "You have just added " + moneyEarned + " to your currency!");
					}
				}
				else
				{
					event.getPlayer().sendMessage(ChatColor.RED + "ERROR: Report this to the developer ThatBox.");
				}
			}
		}
	}
}
