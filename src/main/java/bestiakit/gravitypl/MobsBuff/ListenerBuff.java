package bestiakit.gravitypl.MobsBuff;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import bestiakit.gravitypl.Random;

public class ListenerBuff implements Listener {

	@EventHandler
	public void whiterExtraDamage(EntityDamageByEntityEvent e) {
		Entity player = e.getEntity();
		Entity fireball = e.getDamager();

		if (!(e.getCause() == DamageCause.PROJECTILE))
			return;
		if (!(player instanceof Player && fireball instanceof WitherSkull))
			return;
		e.setDamage(25);
		Player hurt = (Player) player;
		hurt.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 2));

	}

	@EventHandler
	public void enderDragonExtraDamage(EntityDamageByEntityEvent e) {
		Entity player = e.getEntity();
		Entity fireball = e.getDamager();
		if (!(e.getCause() == DamageCause.PROJECTILE))
			return;
		if (!(player instanceof Player && fireball instanceof DragonFireball))
			return;
		e.setDamage(20);
	}

	@EventHandler
	public void enderDragonMobSpawning(ProjectileHitEvent e) {
		Entity dragonFireball = e.getEntity();
		if (!(dragonFireball instanceof DragonFireball))
			return;
		Block b = e.getHitBlock();
		Location bLoc = (Location) b.getLocation();
		for (int i = 0; i >= 10; i++) {
			boolean random = new Random().randomOpportunity();
			if (random) {
				b.getWorld().spawnEntity(bLoc, EntityType.SKELETON_HORSE, random);
			}else {
				b.getWorld().spawnEntity(bLoc, EntityType.CREEPER, random);
			}
		}
		b.getWorld().strikeLightning(bLoc);

	}
}
