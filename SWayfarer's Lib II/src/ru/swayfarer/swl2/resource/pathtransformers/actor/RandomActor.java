package ru.swayfarer.swl2.resource.pathtransformers.actor;

import java.util.UUID;

/**
 * Актер %rand%, который заменяется {@link UUID#randomUUID()}
 * @author swayfarer
 */
public class RandomActor implements IPathActor {

	/** Получить строку после трансформации актрера */
	@Override
	public String getTransformedString()
	{
		return UUID.randomUUID().toString();
	}

	/** Получить строку до трансформации актрера */
	@Override
	public String getUnransformedString()
	{
		return "%rand%";
	}

	/** Получить строку после трансформации актрера */
	@Override
	public boolean isPackingSupported()
	{
		return false;
	}

	
	
}
