package bestiakit.gravitypl;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class ListenerNightmares implements Listener {
	
	
	Plugin plugin;
	boolean arrowsOnTheBedNightmareIsEnabled = true;
	boolean fallingNightmareIsEnabled = true;
	
	
	public ListenerNightmares(Plugin plugin, List<String> allowedNightmares)
	{
		this.plugin = plugin;
		
		
		for (String s : allowedNightmares)
		{
			if(s.equals("arrowsOnTheBed")) arrowsOnTheBedNightmareIsEnabled = true;
			if(s.equals("fallingNightmare")) fallingNightmareIsEnabled = true;
		}
	}
	
	
	
	public void nightmareWithArrows(Player player, Location loc)
	{
		if(!arrowsOnTheBedNightmareIsEnabled) return;
		
		
		double nloc = player.getLocation().getY() + 2;
		loc.setY(nloc);
		//todo: cannot spawn arrow with null velocity 
		player.getWorld().spawnArrow(loc, null, 1, 0);
	}
	
	public void nightmareFalling(Player player,PlayerBedEnterEvent e, Location loc)
	{
		if(!fallingNightmareIsEnabled) return;
		player.sendMessage(ChatColor.DARK_BLUE + "Sientes una presencia...");
		e.setCancelled(true);
		
		Location firstLoc = (Location)player.getLocation();
		double twoBlocksUp = firstLoc.getY() + 2;
		firstLoc.setY(twoBlocksUp);
		
		Random random = new Random();
		if(random.randomOpportunity()) loc.setY(-90);
		else loc.setY(250);
	
		
		
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,200,20));
		//player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,200,10));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,200,10));
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,200,10));
		player.teleport(loc);
		
		new BukkitRunnable() {
            @Override
            public void run() {
            	player.teleport(firstLoc);
            }
        }.runTaskLater(plugin, 160);
	}
	
	public void newNightmare(Player player,int i,PlayerBedEnterEvent e, Location loc)
	{
		switch (i) {
		case 0:
			nightmareWithArrows(player, loc);
			break;
		case 1:
			nightmareFalling(player,e, loc);
			break;
		default:
			nightmareWithArrows(player, loc);
			break;
		}
	}
	
	@EventHandler
	public void onPlayerSleeping(PlayerBedEnterEvent e)
	{
		if(!(e.getPlayer() instanceof Player))	return;
		if(!(e.getBedEnterResult() == BedEnterResult.OK)) return;
		Player sleeper = (Player)e.getPlayer();
		
		Random random = new Random();
		
		if(random.randomNumber(10) != 2) return;
		//random.randomNumber(5);
		//fix para que, cuando encola el sueño, si encola 2 sueños el ultimo sueño no guarde la posición del primer sueño
		Location loc = (Location)sleeper.getLocation();
		new BukkitRunnable() {
            @Override
            public void run() {
            	
            	newNightmare(sleeper,1,e,loc);
            }
        }.runTaskLater(plugin, 40);
		
		
		
	}
	
}

