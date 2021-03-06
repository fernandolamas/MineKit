package bestiakit.gravitypl;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import bestiakit.gravitypl.SteveArrows.SteveArrowsListener;


public class MineKit extends JavaPlugin {
	

	@Override
	public void onEnable()
	{
		this.saveDefaultConfig();
		List<String> effectslist = this.getConfig().getStringList("allowedEffects");
		String amountOfParticlesHearth = this.getConfig().getString("amountOfParticlesHearth");
		
		String PickaxeClimbingCD = this.getConfig().getString("pickaxeClimbingTimesAllowed");
		String PickaxeClimbingDebugMode = this.getConfig().getString("pickaxeClimbingDebugMode");
		String PickaxeClimbingIsEnabled = this.getConfig().getString("PickaxeClimbingIsEnabled");
		
		String LightningCreeperIsEnabled = this.getConfig().getString("LightningCreeperIsEnabled");
		String creeperProbConfig = this.getConfig().getString("CreeperProbabilityToGetLighted");
		
		String isSnowEnabledConfig = this.getConfig().getString("SnowCustomFrostDamageEnabled");
		
		
		List<String> nightmareList = this.getConfig().getStringList("allowedNightmares");
		
		getServer().getPluginManager().registerEvents(new ListenerClimbing(PickaxeClimbingCD, PickaxeClimbingDebugMode, PickaxeClimbingIsEnabled, this), this);
		getServer().getPluginManager().registerEvents(new StatusListener(effectslist, this, amountOfParticlesHearth), this);
		getServer().getPluginManager().registerEvents(new ListenerNightmares(this, nightmareList), this);
		getServer().getPluginManager().registerEvents(new LightningCreeperListener(LightningCreeperIsEnabled, creeperProbConfig,this), this);
		getServer().getPluginManager().registerEvents(new SteveArrowsListener(this),this);
		getServer().getPluginManager().registerEvents(new ListenerSnow(this, isSnowEnabledConfig), this);
		
		
		this.getCommand("bestiakit").setExecutor(new CommandExecutioner(this));
	} 
	
	@Override
	public void onDisable()
	{
		getLogger().info("xD");
	}
	
	
	
}