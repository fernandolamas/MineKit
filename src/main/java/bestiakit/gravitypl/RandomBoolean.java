package bestiakit.gravitypl;

import java.util.concurrent.ThreadLocalRandom;

public class RandomBoolean {
	public boolean randomOpportunity()
	{
		return ThreadLocalRandom.current().nextBoolean();
	}
}
