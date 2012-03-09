package menexia.jminer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class jMinerCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String zhf, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("[jMiner] You must be in-game to use jMiner!");
			return true;
		}
		if (args.length == 0 || (args.length == 1 && args[1].equalsIgnoreCase("verbose"))) {
			jMiner.f.add((Player)sender);
			return true;
		}
		return false;
	}

}
