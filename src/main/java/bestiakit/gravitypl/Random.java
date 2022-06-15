package bestiakit.gravitypl;

import java.util.concurrent.ThreadLocalRandom;

public class Random {
	public boolean randomOpportunity()
	{
		return ThreadLocalRandom.current().nextBoolean();
	}
	
	public int randomNumber(int i)
	{
		return ThreadLocalRandom.current().nextInt(i);
	}
}
