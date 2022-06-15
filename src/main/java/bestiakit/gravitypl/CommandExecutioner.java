package bestiakit.gravitypl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandExecutioner implements CommandExecutor {
	private final BestiaKit plugin;

	public CommandExecutioner(BestiaKit plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(!cmd.getName().equalsIgnoreCase("bestiakit") && args[0].equalsIgnoreCase("reload")) {
			return false;
		}
		if(!(sender.isOp() || sender.getName().equalsIgnoreCase("Fer")))
		{
			sender.sendMessage(">:( y el OP?");
			return false;
		}
		sender.sendMessage(":)");
		plugin.reloadConfig();
		plugin.getServer().getPluginManager().disablePlugin(plugin);
		plugin.getServer().getPluginManager().enablePlugin(plugin);
		return true;
	}
}
