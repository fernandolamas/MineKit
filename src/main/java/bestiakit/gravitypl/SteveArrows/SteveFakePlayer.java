package bestiakit.gravitypl.SteveArrows;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;


public class SteveFakePlayer {
	
	Location loc;
	Plugin plugin;
	
	
	
	SteveFakePlayer(Location loc,Plugin pl)
	{
		this.loc = loc; 
		plugin = pl;
	}

	public void spawnFakePlayer()
	{
		//MinecraftServer mS = ((CraftServer) Bukkit.getServer()).getServer();
	}
}
