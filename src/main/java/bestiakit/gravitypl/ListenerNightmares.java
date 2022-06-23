package bestiakit.gravitypl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class ListenerNightmares implements Listener {

	Plugin plugin;
	private boolean arrowsOnTheBedNightmareIsEnabled = true;
	private boolean fallingNightmareIsEnabled;
	private boolean playerIsHavingNightmare;
	private boolean mobsOnTheBed;
	private boolean paralisisIsEnabled;

	public ListenerNightmares(Plugin plugin, List<String> allowedNightmares) {
		this.plugin = plugin;

		for (String s : allowedNightmares) {
			if (s.equalsIgnoreCase("arrowsOnTheBed"))
				arrowsOnTheBedNightmareIsEnabled = true;
			if (s.equalsIgnoreCase("fallingNightmare"))
				fallingNightmareIsEnabled = true;
			if (s.equalsIgnoreCase("mobsOnTheBed"))
				mobsOnTheBed = true;
			if (s.equalsIgnoreCase("paralisis"))
				paralisisIsEnabled = true;
		}
	}

	public void nightmareWithArrows(Player player, Location loc) {
		if (!arrowsOnTheBedNightmareIsEnabled)
			return;

		double nLoc = player.getLocation().getY() + 2;
		loc.setY(nLoc);
		Vector nV = loc.getDirection().clone();

		for (int i = 0; i <= 2; i++) {
			player.getWorld().spawnArrow(loc, nV, 0.6f, 12f);
			player.playEffect(player.getLocation(), Effect.FIREWORK_SHOOT, null);
		}
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
		if (!paralisisIsEnabled)
			return;
		// p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,80,1));
		p.setFireTicks(80);
	}

	private void nightmareSummoning(Player p) {
		if (!mobsOnTheBed)
			return;
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
					if (s.getType() == nightmareMob) {
						plugin.getLogger().log(Level.INFO, "despawned" + s.getType());
						s.remove();
						p.setInvulnerable(false);
						if (p.isOnline())
							;
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

		if (random.randomNumber(7) != 2)
			return;
		playerIsHavingNightmare = true;

		Location loc = (Location) sleeper.getLocation();
		new BukkitRunnable() {
			@Override
			public void run() {
				int r = random.randomNumber(4);
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
				if (e.getPlayer().isOnline())
					playerIsHavingNightmare = false;
			}
		}.runTaskLater(plugin, 120);
		e.setCancelled(true);
	}

	@EventHandler
	public void playerDamagedDuringSleep(EntityDamageByEntityEvent e) {
		if (!(playerIsHavingNightmare))
			return;

		if (!(e.getEntity() instanceof Player))
			return;

		if (e.getDamager() instanceof Arrow)
			return;

		e.setDamage(0);
		e.setCancelled(true);
	}

	Location pDisconnectedLoc;

	@EventHandler
	public void playerDisconnectedDuringNightmare(PlayerQuitEvent e) {
		Player p = (Player) e.getPlayer();
		if (p.isOp())
			return;
		p.setInvulnerable(false);
		pDisconnectedLoc = (Location) p.getLocation();
	}

	boolean chunkIsLoaded = true;

	@EventHandler
	public void playerReconnectedDuringNightmare(ChunkUnloadEvent e) {
		if (!chunkIsLoaded)
			return;
		if (!playerIsHavingNightmare)
			return;
		if (pDisconnectedLoc == null)
			return;
		e.getChunk().load();

		// TODO: test if loads and unload chunk correctly
		plugin.getLogger().log(Level.INFO,
				" chunk reloaded by player disconnected during nightmare at location: " + pDisconnectedLoc);
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!chunkIsLoaded)
					return;
				plugin.getLogger().log(Level.INFO, " Unloading chunk at location: " + pDisconnectedLoc);
				e.getChunk().unload(true);
				chunkIsLoaded = false;
			}
		}.runTaskLater(plugin, 200);

	}

}
