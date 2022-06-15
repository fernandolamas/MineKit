package bestiakit.gravitypl;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ListenerClimbing implements Listener{
	
	@EventHandler
	public void playerClimbingWithPickaxe(PlayerInteractEvent e)
	{
		if(!(e.getPlayer() instanceof Player))
		{
			return;
		}
		
		Player player = (Player) e.getPlayer();

		if(!(player.getInventory().getItemInMainHand().getType() instanceof Material))
		{
			return;
		}
		Material material = (Material)player.getInventory().getItemInMainHand().getType();
		if(!(material == Material.STONE_PICKAXE))
		{
			return;
		}
		
		if(!(e.getClickedBlock() instanceof Block))
		{
			return;
		}
		
		Location loc = (Location)player.getLocation();
		World world = (World)player.getWorld();
		
		Block rightClickedBlock = e.getClickedBlock();
		
		if(!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
		{
			return;
		}
		
		Location playerLoc = player.getLocation();
		playerLoc.setY(rightClickedBlock.getLocation().getBlockY());
		player.teleport(playerLoc);
		
		
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,100,1));
		world.playEffect(loc,Effect.CRIT, 0 );
		player.sendMessage("--Clavaste el pico en la piedra--");
	}
}
