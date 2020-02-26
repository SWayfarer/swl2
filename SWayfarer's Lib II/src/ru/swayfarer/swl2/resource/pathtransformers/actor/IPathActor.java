package ru.swayfarer.swl2.resource.pathtransformers.actor;

public interface IPathActor {

	/** Получить трансформированную строку */
	public String getTransformedString();
	
	/** Получить не-трансформированную строку */
	public String getUnransformedString();

	/** Поддерживается ли обратная трансформация?  */
	public boolean isPackingSupported();
	
}
