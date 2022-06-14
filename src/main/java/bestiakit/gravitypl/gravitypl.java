package bestiakit.gravitypl;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class gravitypl extends JavaPlugin {
	
	
	
	public class RandomBoolean {

	}

	@Override
	public void onEnable()
	{
		getLogger().info("onEnable has been invoked!");
//		HashMap<String> playerList = new HashMap<String>();
		
		
		/*for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			//playerList.put(player.getName(), playerData(player));
			playerList.put(player.getName(), playerData(player));
		}*/
		getServer().getPluginManager().registerEvents((Listener) this, this);
		new ListenerClimbing(this);
		new StatusListener(this);
		
	}
	
	@Override
	public void onDisable()
	{
		getLogger().info("onDisable has been invoked!");
	}
	
	
	
}