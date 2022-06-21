package bestiakit.gravitypl.SteveArrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;


public class SteveArrowsListener implements Listener{
	
	Plugin pl;
	HashMap<String,Player> ArrowEffects = new HashMap<String,Player>();
	
	public SteveArrowsListener(Plugin plugin)
	{
		pl = plugin;
	}
	
	@EventHandler
	public void SteveShootingTheArrow(EntityShootBowEvent e)
	{
		if(!(e.getEntity() instanceof Player)) return;
		if(!(e.getProjectile() instanceof Arrow)) return;
		Arrow a = (Arrow) e.getProjectile();
		Player p = (Player)e.getEntity();
		
		HashMap<Integer, ? extends ItemStack> arrows = p.getInventory().all(Material.ARROW);
		
		for(ItemStack value : arrows.values())
		{
			if((ArrowEffects.containsKey(value.getItemMeta().getDisplayName()) && ArrowEffects.containsValue(p))) return;
			ArrowEffects.put(value.getItemMeta().getDisplayName(),p);
		}
	}

	@EventHandler
	public void SteveArrowTouchingTheGround(ProjectileHitEvent e)
	{
		//if(!isAllowedAlgoMas) return;
		if(!(e.getEntity().getShooter() instanceof Player)) return;
		Player p = (Player)e.getEntity().getShooter();
		
		if(!(e.getEntity() instanceof Arrow)) return;
		Arrow arrow = (Arrow)e.getEntity();
		if(e.getHitBlock() == null) return;
		Block b = e.getHitBlock();
		//p.sendMessage("Hiteaste un: " + e.getHitBlock());
		
		//if(!(b.getType() == Material.DIRT)) return;
		
		//b.setType(Material.MAGMA_BLOCK);
		Location bLoc = (Location)b.getLocation();
		
		//Get arrow name from variable list ArrowEffects
		if(ArrowEffects.isEmpty())
			{
				return;
			}
		for(Entry<String, Player> ps : ArrowEffects.entrySet())
		{
			if(ps.getKey().equalsIgnoreCase("Thor"))
			{
				if(!(ps.getValue().hasPermission("bestiakit.arrow.thunderarrow"))) return; 
				else arrow.getWorld().strikeLightning(bLoc);
			}
			if(ps.getKey().equalsIgnoreCase("Explosion"))
			{
				if(!(ps.getValue().hasPermission("bestiakit.arrow.explosion"))) return;
				else arrow.getWorld().createExplosion(bLoc, 4);
			}
		}
	}
}
