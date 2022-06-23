package bestiakit.gravitypl;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class ListenerSnow implements Listener {
	// != Material.LEGACY_SNOW)
	// bUPT != Material.LEGACY_SNOW_BLOCK)

	Plugin plugin;
	int counter = 0;
	boolean isFreezing;
	boolean isSnowEnabled = true;

	public ListenerSnow(Plugin pl, String isSnowEnabledConfig) {
		plugin = pl;
		isSnowEnabled = Boolean.parseBoolean(isSnowEnabledConfig);
	}

	@EventHandler
	public void playerIsOnSnowBiome(PlayerMoveEvent e) {
		if(!isSnowEnabled) return; 
		if(isFreezing) return;
		Player p = e.getPlayer();

		Biome b = p.getWorld().getBiome(p.getLocation());

		if (!(b == Biome.SNOWY_BEACH || b == Biome.SNOWY_PLAINS || b == Biome.SNOWY_SLOPES || b == Biome.SNOWY_TAIGA
				|| b == Biome.GROVE || b == Biome.ICE_SPIKES))
			return;
		Location loc = (Location) e.getFrom();

		double snowBlockLoc = loc.getY() - 1;
		loc.setY(snowBlockLoc);

		Block blockUnderPlayer = p.getWorld().getBlockAt(loc);
		Material bUPT = (Material) blockUnderPlayer.getType();
		if (!(bUPT == Material.SNOW || bUPT == Material.SNOW_BLOCK))
			return;

		if (!(p.getInventory().getBoots() == null))
			return;
		
		if(!p.isFrozen())
		{
			p.sendMessage(ChatColor.ITALIC + " " + ChatColor.BLUE + "Tus pies se congelan");
		}
		p.setFreezeTicks(300);
	}
	
	@EventHandler
	public void playerIsTakingDamageByFrost(EntityDamageEvent e)
	{
		if(!(e.getCause() == DamageCause.FREEZE)) return;
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player)e.getEntity();
		
		Block checkWater = p.getEyeLocation().getBlock();
        if(!(checkWater.getType() == Material.WATER)) return;
        e.setDamage(3);
        p.sendMessage(ChatColor.BLUE+ "" + ChatColor.ITALIC + "El agua esta contrayendo tus musculos");
	}

	/*
	@EventHandler
	public void playerIsStartingToFreeze(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		p.sendMessage("Freeze ticks: " + p.getFreezeTicks());
	}*/

}
