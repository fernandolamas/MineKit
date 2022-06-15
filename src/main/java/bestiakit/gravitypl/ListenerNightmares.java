package bestiakit.gravitypl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class ListenerNightmares implements Listener {
	
	
	public void nightmareWithArrows(Player player)
	{
		Location loc = player.getLocation();
		double nloc = player.getLocation().getY() + 2;
		loc.setY(nloc);
		player.getWorld().spawnArrow(loc, null, 0, 0);
	}
	
	public void newNightmare(Player player,int i)
	{
		switch (i) {
		case 0:
			nightmareWithArrows(player);
			break;

		default:
			nightmareWithArrows(player);
			break;
		}
	}
	
	@EventHandler
	public void onPlayerSleeping(final PlayerBedEnterEvent e)
	{
		if(!(e.getPlayer() instanceof Player))	return;
		if(!(e.getPlayer().isSleeping())) return;
		
		Player sleeper = (Player)e.getPlayer();
		
		Random random = new Random();
		
		if(!(random.randomOpportunity())) return;
		
		//random.randomNumber(5);
		newNightmare(sleeper,0);
		
		
	}
	
}
