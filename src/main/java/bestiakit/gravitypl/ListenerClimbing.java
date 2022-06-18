package bestiakit.gravitypl;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ListenerClimbing implements Listener{
	
	int cooldown = 0;
	int maxCooldown = 0;
	boolean debug = false;
	boolean isEnabled = true;
	Plugin pl;
	
	ListenerClimbing(String PickaxeClimbingCD,String PickaxeClimbingDebugMode,String PickaxeClimbingIsEnabled, Plugin plugin)
	{
		maxCooldown = Integer.parseInt(PickaxeClimbingCD);
		debug = Boolean.parseBoolean(PickaxeClimbingDebugMode);
		isEnabled = Boolean.parseBoolean(PickaxeClimbingIsEnabled);
		pl = plugin;
	}
	@EventHandler
	public void playerHasStonePickaxeInHand(EntityPickupItemEvent e)
	{
		if(!(e.getEntity() instanceof Player)) return;
		Player p = (Player)e.getEntity();
		
		ItemStack pickedItem = e.getItem().getItemStack();
		
		if(!(pickedItem.getType() == Material.STONE_PICKAXE)) return;

		Random random = new Random();
		
		if(random.randomOpportunity())
		{
			p.sendMessage(ChatColor.BOLD + "" + ChatColor.BLUE + "Es posible que...");
	        new BukkitRunnable() {
	            
	            @Override
	            public void run() {
	            	p.sendMessage(ChatColor.BOLD + "" + ChatColor.BLUE + "...poniendo otro pico de piedra en tu otra mano...");
	            }
	            
	        }.runTaskLater(pl, 100);
	        new BukkitRunnable() {
	            
	            @Override
	            public void run() {
	            	p.sendMessage(ChatColor.BOLD + "" + ChatColor.BLUE + "...con ambos picos puedas escalar paredes de piedra.");
	            }
	            
	        }.runTaskLater(pl, 200);
		}
	}
	
	@EventHandler
	public void playerClimbingWithPickaxe(PlayerInteractEvent e)
	{
		if(!isEnabled) return;

		
		if(!(e.getPlayer() instanceof Player)) return;
		
		Player player = (Player) e.getPlayer();
		

		if(!(player.getInventory().getItemInMainHand().getType() instanceof Material)) return;
		Material mainHandItem = (Material)player.getInventory().getItemInMainHand().getType();
		if(!(mainHandItem == Material.STONE_PICKAXE)) return;
		Material offHandItem = (Material)player.getInventory().getItemInOffHand().getType();
		if(!(offHandItem == Material.STONE_PICKAXE)) return;
		
		if(!(e.getClickedBlock() instanceof Block)) return;
		
		Block rightClickedBlock = e.getClickedBlock();
		
		Material blockType = rightClickedBlock.getType(); 
		
		
		//TODO: add configuration for allowed climbing blocks
		if(!(blockType.equals(Material.STONE)||blockType.equals(Material.SANDSTONE)||blockType.equals(Material.COBBLESTONE))) return;
		
		
		
		Location loc = (Location)player.getLocation();
		World world = (World)player.getWorld();
		
		
		if(!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
		
		
		
		if(debug) player.sendMessage("Cooldown is: " + cooldown);
		
		if(cooldown == maxCooldown)
		{
			if(!player.isOnGround()) return;
			cooldown = 0;
		}
		
		Location playerLoc = player.getLocation();
		int blockWithPickaxeAttached = rightClickedBlock.getLocation().getBlockY();

		
		
		playerLoc.setY(blockWithPickaxeAttached);
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,100,1));
		world.playEffect(loc,Effect.ANVIL_LAND, 0 );
		cooldown++;
		
		double blockAboveRCBlockY = rightClickedBlock.getLocation().getBlockY() + 3;
		double blockAboveRCBlockX = rightClickedBlock.getLocation().getBlockX() + 0.5;
		double blockAboveRCBlockZ = rightClickedBlock.getLocation().getBlockZ() + 0.5;
		Location blockAbove = rightClickedBlock.getLocation();
		blockAbove.setY(blockAboveRCBlockY);
		blockAbove.setX(blockAboveRCBlockX);
		blockAbove.setZ(blockAboveRCBlockZ);
		
		
		//check if block above the clicked block is air
		//TODO: fixear que el bloque de arriba sea aire y no piedra(ocualquierboloque)
		if(blockAbove.getBlock().isEmpty())
		{
			player.teleport(blockAbove);			
		}else {
			player.teleport(playerLoc);
		}
	}
}
