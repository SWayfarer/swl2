package ru.swayfarer.swl2.asm.injection;

/**
 * Контроллер для результата работы метода. 
 * @author swayfarer
 *
 */
public class ReturnContainer {

	/** Изменялось ли значение контейнера */
	protected boolean isSetted = false;
	
	/** Значение контейнера */
	protected Object value;
	
	/** Контроллер вернет value*/
	public void setReturn() 
	{
		this.isSetted = true;
	}
	
	/** Задать возвращаемое значение. <h1>Внимание:</h1> InjectionAsm не проверяет совпадение возвращаемго типа метода и типа returnValue! При некорректном использовании можно сломать класс.*/
	public void setValue(Object value)
	{
		isSetted = true;
		this.value = value;
	}
	
	/** Получить возвращаемое значение */
	@SuppressWarnings("unchecked")
	public <T> T getValue()
	{
		return (T)value;
	}
	
	/** Было ли задано возвращаемое значение */
	public boolean isSetted()
	{
		return isSetted;
	}
	
}
