package bestiakit.gravitypl;

import java.util.List;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StatusListener implements Listener{
	
	boolean isAllowedFightEffects;
	boolean isAllowedArrowEffects;

	public StatusListener(List<String> effects) {
		for (String s : effects)
		{
			if(s.equals("fight"))
			{
				isAllowedFightEffects = true;
			}
			if(s.equals("arrow"))
			{
				isAllowedArrowEffects = true;
			}
		}
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
		
		if(damagerPlayer.getWorld().hasStorm())
		{
			damagerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100,1));
			takerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100,1));
		}
	}

	@EventHandler
	public void playerHitedAnotherPlayer(EntityDamageByEntityEvent e) {
		
		if(!isAllowedFightEffects)
		{
			return;
		}
		
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
		
		Random rBool = new Random();
		if(rBool.randomOpportunity())
		{
			applyPotionsDuringFight(damagerPlayer, takerPlayer);
		}	
	
	}
	
	@EventHandler
	public void playerBeignHitByArrowInTheHead(EntityDamageByEntityEvent e)
	{
		if(!isAllowedArrowEffects)
		{
			return;
		}
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
