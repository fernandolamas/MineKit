package bestiakit.gravitypl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class ListenerNightmares implements Listener {

	Plugin plugin;
	boolean arrowsOnTheBedNightmareIsEnabled = true;
	boolean fallingNightmareIsEnabled = true;
	private boolean playerIsHavingNightmare;

	public ListenerNightmares(Plugin plugin, List<String> allowedNightmares) {
		this.plugin = plugin;

		for (String s : allowedNightmares) {
			if (s.equals("arrowsOnTheBed"))
				arrowsOnTheBedNightmareIsEnabled = true;
			if (s.equals("fallingNightmare"))
				fallingNightmareIsEnabled = true;
		}
	}

	public void nightmareWithArrows(Player player, Location loc) {
		if (!arrowsOnTheBedNightmareIsEnabled)
			return;

		double nloc = player.getLocation().getY() + 2;
		loc.setY(nloc);
		// todo: cannot spawn arrow with null velocity
		// player.getWorld().spawnArrow(loc, null, 1, 0);

	}

	public void nightmareFalling(Player player, PlayerBedEnterEvent e, Location loc) {
		if (!fallingNightmareIsEnabled)
			return;
		player.sendMessage(ChatColor.DARK_BLUE + "Sientes una presencia...");
		e.setCancelled(true);

		Location firstLoc = (Location) player.getLocation();
		double twoBlocksUp = firstLoc.getY() + 2;
		firstLoc.setY(twoBlocksUp);

		Random random = new Random();
		if (random.randomOpportunity())
			loc.setY(-90);
		else
			loc.setY(250);

		player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 20));
		// player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,200,10));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 200, 10));
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 10));
		player.teleport(loc);

		new BukkitRunnable() {
			@Override
			public void run() {
				player.teleport(firstLoc);
			}
		}.runTaskLater(plugin, 160);
	}

	public void nightmareParalisis(Player p, PlayerBedEnterEvent e) {
		// p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,80,1));
		p.setFireTicks(80);
	}

	private void nightmareSummoning(Player p) {
		//TODO: no es suficiente una poción de invisibilidad. hay que hacer inmune al player.
		p.setInvulnerable(true);
		EntityType nightmareMob;
		Random r = new Random();
		int nR = r.randomNumber(3);
		switch (nR) {
		case 0:
			nightmareMob = EntityType.ZOMBIE;
			break;
		case 1:
			nightmareMob = EntityType.BLAZE;
			break;
		case 2:
			nightmareMob = EntityType.CREEPER;
			break;
		default:
			nightmareMob = EntityType.FOX;
			break;
		}
		ArrayList<Entity> mobsSpawnedDuringNightmare = new ArrayList<Entity>();
		for (int i = 0; i <= 10; i++) {
			Entity e = p.getWorld().spawnEntity(p.getLocation(), nightmareMob, true);
			mobsSpawnedDuringNightmare.add(e);
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				plugin.getLogger().log(Level.INFO, "despawning mobs");
				for (Entity s : mobsSpawnedDuringNightmare) {
					if (s.getType() == nightmareMob)
					{
						plugin.getLogger().log(Level.INFO, "despawned" + s.getType());
						s.remove();
						p.setInvulnerable(false);
						if(p.isOnline());
					}
				}
			}
		}.runTaskLater(plugin, 120);
	}

	public void newNightmare(Player player, int i, PlayerBedEnterEvent e, Location loc) {
		switch (i) {
		case 0:
			nightmareWithArrows(player, loc);
			break;
		case 1:
			nightmareFalling(player, e, loc);
			break;
		case 2:
			nightmareParalisis(player, e);
			break;
		case 3:
			nightmareSummoning(player);
			break;
		default:
			break;
		}
	}

	@EventHandler
	public void onPlayerSleeping(PlayerBedEnterEvent e) {
		if (!(e.getPlayer() instanceof Player))
			return;
		if (!(e.getBedEnterResult() == BedEnterResult.OK))
			return;
		Player sleeper = (Player) e.getPlayer();

		Random random = new Random();

		//DESCOMENTAR ANTES DE DEPLOYAR if(random.randomNumber(10) != 2) return;
		playerIsHavingNightmare = true;
		
		Location loc = (Location) sleeper.getLocation();
		new BukkitRunnable() {
			@Override
			public void run() {
				int r = random.randomNumber(4);
				r = 3;
				sleeper.sendMessage("Random number is: " + r);
				newNightmare(sleeper, r, e, loc);
			}
		}.runTaskLater(plugin, 40);
	}

	@EventHandler
	public void playerIsHavingNightmare(PlayerBedLeaveEvent e) {
		if (!playerIsHavingNightmare)
			return;
		new BukkitRunnable() {
			@Override
			public void run() {
				if(e.getPlayer().isOnline()) playerIsHavingNightmare = false;
			}
		}.runTaskLater(plugin, 120);
		e.setCancelled(true);
	}
	@EventHandler
	public void playerDamagedDuringSleep(EntityDamageByEntityEvent  e)
	{
		if(!(playerIsHavingNightmare)) return;
		if(!(e.getEntity() instanceof Player)) return;
		e.setDamage(0);
		e.setCancelled(true);
	}
	
	Location pDisconnectedLoc;
	@EventHandler
	public void playerDisconnectedDuringNightmare(PlayerQuitEvent e)
	{
		Player p = (Player)e.getPlayer();
		if(p.isOp()) return;
		p.setInvulnerable(false);
		pDisconnectedLoc = (Location)p.getLocation();
	}
	
	@EventHandler
	public void playerReconnectedDuringNightmare(ChunkUnloadEvent e)
	{
		if(!playerIsHavingNightmare) return;
		if(pDisconnectedLoc == null) return;
		e.getChunk().load();
		
		//TODO: test if loads and unload chunk correctly
		plugin.getLogger().log(Level.INFO," chunk reloaded by player disconnected during nightmare at location: " + pDisconnectedLoc);
		new BukkitRunnable() {
			@Override
			public void run() {
				boolean isUnloaded = false;
				Exception error = null;
				try {
					isUnloaded = e.getChunk().unload(true);
				} catch (Exception e2) {
					error = e2;
				}
				if(isUnloaded)
				{
					plugin.getLogger().log(Level.INFO," chunk unloaded successfully " + pDisconnectedLoc);
				}else {
					plugin.getLogger().log(Level.WARNING," error unloading chunk at " + pDisconnectedLoc + " due to error: "+ error.getMessage());
				}

			}
		}.runTaskLater(plugin, 200);
		
	}
	

}
