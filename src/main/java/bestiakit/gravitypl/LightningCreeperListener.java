package bestiakit.gravitypl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

public class LightningCreeperListener implements Listener{
	
	boolean isEnabled = true;
	int creeperProbability = 100;
	
	public LightningCreeperListener(String isEnabledLC,String creeperProbConfig,Plugin pl) 
	{
		isEnabled = Boolean.parseBoolean(isEnabledLC);
		creeperProbability = Integer.parseInt(creeperProbConfig);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void thunderLightCreeperOnEyeWacht(PlayerMoveEvent event)
	{
		if(!isEnabled) return;
		Player player = (Player)event.getPlayer();
		Entity e = getTarget(player, 20);
		if(e == null) return;
		if(!(e.toString().equals("CraftCreeper"))) return;
		
		Creeper creeper = (Creeper)e;
		
		
		if(creeper.isPowered()) return;
		Random random = new Random();
		
		//probabilidad
		if(random.randomNumber(creeperProbability) != 2) return;
		
		e.getWorld().strikeLightning(e.getLocation());
		creeper.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,60,2));
			
	}
	
    Entity getTarget(Player player,int range) {
        List<Entity> nearbyE = player.getNearbyEntities(range,
        		range, range);
        ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();

        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
                livingE.add((LivingEntity) e);
            }
        }

        Entity target = null;
        BlockIterator bItr = new BlockIterator(player, range);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
                block = bItr.next();
                bx = block.getX();
                by = block.getY();
                bz = block.getZ();
                        // check for entities near this block in the line of sight
                        for (LivingEntity e : livingE) {
                                loc = e.getLocation();
                                ex = loc.getX();
                                ey = loc.getY();
                                ez = loc.getZ();
                                if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) {
                                        // entity is close enough, set target and stop
                                        target = e;
                                        return target;
                                }
                        }
                }
        	return null;
            }
}
