package bestiakit.gravitypl;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;


public class BestiaKit extends JavaPlugin {
	

	@Override
	public void onEnable()
	{
		this.saveDefaultConfig();
		List<String> effectslist = this.getConfig().getStringList("allowedEffects");
		getServer().getPluginManager().registerEvents(new ListenerClimbing(), this);
		getServer().getPluginManager().registerEvents(new StatusListener(effectslist), this);
		getServer().getPluginManager().registerEvents(new ListenerNightmares(), null);
		
		this.getCommand("bestiakit").setExecutor(new CommandExecutioner(this));
		
	}
	
	@Override
	public void onDisable()
	{
		getLogger().info("xD");
	}
	
	
	
}