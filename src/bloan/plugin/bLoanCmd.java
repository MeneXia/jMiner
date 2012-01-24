package bloan.plugin;

import java.util.HashMap;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class bLoanCmd implements CommandExecutor
{
	public HashMap<Player, Double> useLoan = new HashMap<Player, Double>();
	public HashMap<Player, Player> loaner = new HashMap<Player, Player>();
	public HashMap<Player, HashMap<Player,Double>> debt = new HashMap<Player, HashMap<Player, Double>>();
	public HashMap<Player, Double> totalDebt = new HashMap<Player, Double>();
	public HashMap<Player, Double> totalCredit = new HashMap<Player, Double>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cL,
			String[] args) {
		if(!(sender instanceof Player))
		{
			System.out.println("[bLoan] Console may not use this plugin's commands.");
		}
		else
		{
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("loan"))
			{
				if(args.length == 0)
				{
					p.sendMessage(ChatColor.GRAY + "- - - - " + ChatColor.BLUE +"[bLoan] " + ChatColor.GRAY + "- - - -");
					p.sendMessage(ChatColor.BLUE + "/loan - " + ChatColor.GRAY + "Displays help menu.");
					p.sendMessage(ChatColor.BLUE + "/loan <player> <amount> - " + ChatColor.GRAY + "Loan a player money.");
					p.sendMessage(ChatColor.BLUE + "/loan debt - " + ChatColor.GRAY + "Shows your total debt.");
					p.sendMessage(ChatColor.BLUE + "/loan credit - " + ChatColor.GRAY + "Shows total people owe you.");
					p.sendMessage(ChatColor.BLUE + "/debt <player> - " + ChatColor.GRAY + "Shows the debt a player owes.");
					p.sendMessage(ChatColor.BLUE + "/credit <player> - " + ChatColor.GRAY + "Shows how much a player owes you.");
					
				}
				else if(args.length == 2)
				{
					if(p.getServer().getPlayer(args[0]) != null)
					{
						Player loan = p.getServer().getPlayer(args[0]);
						if(args[1] != null)
						{
							
							double amount = Double.parseDouble(args[1]);
							boolean balance = bLoan.economy.has(p.getName(), amount);
							if(balance == true)
							{
								try
								{
									p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] A request has been sent to " + loan.getName() + " for confirmation.");
									loan.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] " +p.getName() + " wants to loan you " + amount + "!");
									loan.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] Type /loan accept to accept the loan.");
									loan.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] Type /loan deny to deny the loan.");
									useLoan.put(loan, amount);
									loaner.put(loan, p);
								}
								catch(NumberFormatException ex)
								{
									p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] Invalid number.");
								}
							}
							else
							{
								p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] Insufficient funds.");
							}
						}
						else
						{
							p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] You must specify an amount!");
						}
					}
					else
					{
						p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] Player either doesn't exist or isn't online.");
					}
				}
				else if(args.length == 1)
				{
					if(args[0].equalsIgnoreCase("accept"))
					{
						if(useLoan.containsKey(p))
						{
							Player player = loaner.get(p);
							EconomyResponse loanPaid = bLoan.economy.depositPlayer(p.getName(), useLoan.get(p));
							EconomyResponse moneyTaken = bLoan.economy.withdrawPlayer(player.getName(), useLoan.get(p));
							if(loanPaid.transactionSuccess() && moneyTaken.transactionSuccess())
							{
								if(totalDebt.get(p) != null)
								{
									totalDebt.put(p, totalDebt.get(p) + useLoan.get(p));
								}
								else
								{
									totalDebt.put(p, useLoan.get(p));
								}
								if(totalCredit.get(p) != null)
								{
									totalCredit.put(player, totalCredit.get(player) + useLoan.get(p));
								}
								else
								{
									totalCredit.put(player, useLoan.get(p));
								}
								debt.put(p, totalDebt);
								debt.get(p).put(player, useLoan.get(p));
								p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] Loan succesful.");
								player.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] The loan was succesful. As a result " + useLoan.get(p) + " was taken from your account.");
								loaner.remove(p);
								useLoan.remove(p);
							}
							else
							{
								p.sendMessage(ChatColor.RED + "ERROR: REPORT THIS TO N0z!");
							}
						}
						else
						{
							p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] You do not have a loan pending.");
						}
					}
					else if(args[0].equalsIgnoreCase("deny"))
					{
						if(useLoan.containsKey(p))
						{
							Player player = loaner.get(p);
							loaner.remove(p);
							useLoan.remove(p);
							p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] You have succesfully denied the loan.");
							player.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] " + p.getName() + " has denied the loan.");
						}
						else
						{
							p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] You do not have a loan pending.");
						}
					}
					else if(args[0].equalsIgnoreCase("debt"))
					{
						if(totalDebt.get(p) != null)
						{
							p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] Your Debt Total:");
							p.sendMessage(ChatColor.GRAY + "Total Amount Owed: " + ChatColor.BLUE + totalDebt.get(p) + ChatColor.GRAY + ".");
							p.sendMessage(ChatColor.GRAY + "Use /debt <Player> to see how much you owe a player.");
						}
						else
						{
							p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] Your Debt Total:");
							p.sendMessage(ChatColor.GRAY + "Total Amount Owed: " + ChatColor.BLUE + "0.00" + ChatColor.GRAY + ".");
							p.sendMessage(ChatColor.GRAY + "Use /debt <Player> to see how much you owe a player.");
						}
					}
					else if(args[0].equalsIgnoreCase("credit"))
					{
						if(totalCredit.get(p) != null)
						{
							p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] Your Credit Total:");
							p.sendMessage(ChatColor.GRAY + "Total Amount Credited: " + ChatColor.BLUE + totalCredit.get(p) + ChatColor.GRAY + ".");
							p.sendMessage(ChatColor.GRAY + "Use /credit <player> to see how much a player owes you.");
						}
						else
						{
							p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] Your Credit Total:");
							p.sendMessage(ChatColor.GRAY + "Total Amount Credited: " + ChatColor.BLUE + "0.00" + ChatColor.GRAY + ".");
							p.sendMessage(ChatColor.GRAY + "Use /credit <player> to see how much a player owes you.");
						}
					}
				}
				return true;
			}
			else if(cmd.getName().equalsIgnoreCase("debt"))
			{
				Player player = p.getServer().getPlayer(args[0]);
				OfflinePlayer offPlayer = p.getServer().getOfflinePlayer(args[0]);
				if(player != null)
				{
					p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] Debt List:");
					p.sendMessage(ChatColor.GRAY + "You owe " + player.getName() + ChatColor.BLUE +debt.get(p).get(player));
					p.sendMessage(ChatColor.GRAY + "Pay them back with /debt <player> pay <amount>");
					if(args[1].equalsIgnoreCase(""))
					{
						
					}
				}
			}
			else if(cmd.getName().equalsIgnoreCase("credit"))
			{
				Player player  = p.getServer().getPlayer(args[0]);
				OfflinePlayer offPlayer = p.getServer().getOfflinePlayer(args[0]);
				if(player != null)
				{
					p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "bLoan" + ChatColor.GRAY + "] Debt List:");
					p.sendMessage(ChatColor.GRAY + player.getName() + " owes you "+ ChatColor.BLUE + debt.get(p).get(player) + ChatColor.GRAY + ".");
					p.sendMessage(ChatColor.GRAY + "Make sure to inform them to pay you back!");
				}
				else
				{
					
				}
			}
		}
		return false;
	}

}
