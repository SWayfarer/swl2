package ru.swayfarer.swl2.resource.pathtransformers.actor;

/** 
 * Простой актер пути
 * @author swayfarer
 */
public class PathActor implements IPathActor {

	/** Строка после преобразования */
	public String trandformedString;
	
	/** Строка до преобразования */
	public String  untransformedString;
	
	/** Поддерживается ли обратная трансформация?  */
	public boolean isPackingSupported = true;
	
	/** Конструктор */
	public PathActor(String trandformedString, String untransformedString)
	{
		super();
		this.trandformedString = trandformedString;
		this.untransformedString = untransformedString;
	}

	/** Получить трансформированную строку */
	@Override
	public String getTransformedString()
	{
		return trandformedString;
	}
	
	/** Получить не-трансформированную строку */
	@Override
	public String getUnransformedString()
	{
		return untransformedString;
	}

	/** Поддерживается ли обратная трансформация?  */
	@Override
	public boolean isPackingSupported()
	{
		return isPackingSupported;
	}

}
