package bestiakit.gravitypl;

import java.awt.Color;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class StatusListener implements Listener{
	
	Plugin plugin;
	boolean isAllowedFightEffects = true;
	boolean isAllowedArrowEffects = true;
	boolean isAllowedRespawnEffects = true;
	boolean isAllowedTeleportEffects = true;

	public StatusListener(List<String> effects,Plugin plugin) {
		this.plugin = plugin;
		for (String s : effects)
		{
			if(s.equals("fight")) isAllowedFightEffects = true;
			if(s.equals("arrow")) isAllowedArrowEffects = true;
			if(s.equals("respawn")) isAllowedRespawnEffects = true;
			if(s.equals("teleport")) isAllowedTeleportEffects = true;
		}
	}

	public void applyPotionsDuringFight(Player damagerPlayer,Player takerPlayer)
	{
		takerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,40,1));
		if(!takerPlayer.hasPotionEffect(PotionEffectType.CONFUSION))
		{
			takerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 4));
		}else takerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 10));
		
		if(damagerPlayer.getWorld().hasStorm())
		{
			damagerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100,1));
			takerPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100,1));
		}
	}

	@EventHandler
	public void playerHitedAnotherPlayer(EntityDamageByEntityEvent e) {
		
		if(!isAllowedFightEffects)	return;
		
		Entity damager = e.getDamager();
		Entity damageTaker = e.getEntity();
		
		if(!(damageTaker instanceof Player)) return;
		if(!(damager instanceof Player)) return;
		
		Player takerPlayer = (Player) damageTaker;
		Player damagerPlayer = (Player) damager;
		
		if(takerPlayer.getLastDamageCause() == null) return;
		
		Random rBool = new Random();
		
		if(rBool.randomOpportunity()) applyPotionsDuringFight(damagerPlayer, takerPlayer);
	
	}
	
	@EventHandler
	public void playerBeignHitByArrowInTheHead(EntityDamageByEntityEvent e)
	{
		if(!isAllowedArrowEffects) return;
		
		Entity player = e.getEntity();
		Entity arrow = e.getDamager();
		
		
		if(!(player instanceof Player && arrow instanceof Arrow)) return;
		
		Player hurt = (Player) player;

		int playerLocation = hurt.getLocation().getBlockY();
		int arrowLocation = arrow.getLocation().getBlockY();		
		
		
		if(playerLocation+2 == arrowLocation)
		{
			Random random = new Random();
			if(hurt.isBlocking())
			{
				if(!random.randomOpportunity()) return;
				hurt.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200,2));
				return;
			}else {
				if(!random.randomOpportunity()) return;
				hurt.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100,5));
			}
		}
	}

    public void addPotionAfterSpawning(Player p)
    {
		if(p.hasPotionEffect(PotionEffectType.SPEED)) return;
		
		p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 150,5));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,1000,1));
		p.sendMessage(ChatColor.BLUE + "Durch die Schmerzen bist du Verwirrt!");
    }
	
	@EventHandler
	public void playerRespawningEvent(PlayerRespawnEvent e)
	{
		if(!isAllowedRespawnEffects) return;
		
		Player p = (Player)e.getPlayer();
		
		new BukkitRunnable() {
            @Override
            public void run() {
            	addPotionAfterSpawning(p);
            }
        }.runTaskLater(plugin, 40);


	}
	
	@EventHandler
	public void playerTeleported(PlayerTeleportEvent e)
	{
		if(!isAllowedTeleportEffects) return;
		Player p = (Player)e.getPlayer();
		if(p.isOp()) return;
		if(!(e.getCause() == TeleportCause.COMMAND || 
		e.getCause() == TeleportCause.PLUGIN || 
		e.getCause() == TeleportCause.END_PORTAL || 
		e.getCause() == TeleportCause.NETHER_PORTAL)) return; 

		Random random = new Random();
		if(random.randomOpportunity()){
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,150,2));
		}
		new BukkitRunnable() {
            @Override
            public void run() {
            	for(int i = 0;i < 100; i++)
            	{
            		Location pLoc = p.getLocation();
            		pLoc.setY(pLoc.getY()+1);
            		p.spawnParticle(Particle.VILLAGER_HAPPY,pLoc, 2, 0.5, 0.5, 0.5);
            	}
            }
        }.runTaskLater(plugin, 5);

	}
	
	@EventHandler
	public void playerGivesToAnotherPlayer(EntityPickupItemEvent e)
	{
		if(!(e.getEntity() instanceof Player)) return;
		
		Player p = (Player)e.getEntity();
		
		ItemStack pickedItem = e.getItem().getItemStack();
		if(!(pickedItem.getType() instanceof Material)) return;
		Material m = pickedItem.getType();
		
		if(m == Material.DIAMOND 
		//|| m == Material.DANDELION 
		//|| m == Material.POPPY
		//|| m == Material.SUNFLOWER
		//|| m == Material.ROSE_BUSH
		//|| m == Material.CORNFLOWER
				)
		{
        	for(int i = 0;i < 100; i++)
        	{
        		Location pLoc = p.getLocation();
        		pLoc.setY(pLoc.getY()+1);
        		p.spawnParticle(Particle.HEART,pLoc, 2, 0.5, 0.5, 0.5);
        	}
		}
		
		
			
	}
	
	@EventHandler
	public void whiterExtraDamage(EntityDamageByEntityEvent e)
	{
		Entity player = e.getEntity();
		Entity fireball = e.getDamager();
		
		
		if(!(e.getCause() == DamageCause.PROJECTILE)) return;
		if(!(player instanceof Player && fireball instanceof WitherSkull)) return;
		e.setDamage(25);
		Player hurt = (Player) player;
		hurt.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,200,2));
		
	}

}
