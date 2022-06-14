package bestiakit.gravitypl;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StatusListener implements Listener{
	

	gravitypl plugin;
	public StatusListener(gravitypl plugin)
	{
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	} 
	
	public void applyPotionsDuringFight(Player damagerPlayer,Player takerPlayer)
	{
		takerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,40,1));
		if(!takerPlayer.hasPotionEffect(PotionEffectType.CONFUSION))
		{
			takerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 4));
		}else {
			takerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 10));
		}
		if(damagerPlayer.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE))
		{
			return;
		}
		damagerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 50, 1));
		
		if(damagerPlayer.getWorld().hasStorm())
		{
			damagerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100,1));
			takerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100,1));
		}
	}

	@EventHandler
	public void playerHitedAnotherPlayer(EntityDamageByEntityEvent e) {
		Entity damager = e.getDamager();
		Entity damageTaker = e.getEntity();
		
		if(!(damageTaker instanceof Player))
		{
			return;
		}
		if(!(damager instanceof Player))
		{
			return;
		}
		
		Player takerPlayer = (Player) damageTaker;
		Player damagerPlayer = (Player) damager;
		
		RandomBoolean rBool = new RandomBoolean();
		if(rBool.randomOpportunity())
		{
			applyPotionsDuringFight(damagerPlayer, takerPlayer);
		}	
	
	}
	
	@EventHandler
	public void playerBeignHitByArrowInTheHead(EntityDamageByEntityEvent e)
	{
		Entity player = e.getEntity();
		Entity arrow = e.getDamager();
		
		
		if(!(player instanceof Player && arrow instanceof Arrow))
		{
			return;
		}
		
		Player hurt = (Player) player;
		/*
		if(!(((Arrow)arrow).getShooter() instanceof Player))
		{
			return;
		}*/
		
		int playerLocation = hurt.getLocation().getBlockY();
		int arrowLocation = arrow.getLocation().getBlockY();		
		
		if(playerLocation+2 == arrowLocation)
		{
			hurt.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100,5));
		}
	}
	

}
