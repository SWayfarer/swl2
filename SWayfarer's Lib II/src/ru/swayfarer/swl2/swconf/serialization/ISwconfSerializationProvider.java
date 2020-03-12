package ru.swayfarer.swl2.swconf.serialization;

import ru.swayfarer.swl2.swconf.primitives.SwconfPrimitive;

/**
 * Провайдер для (де)сериализации объектов через примитивы Swconf
 * @author swayfarer
 *
 * @param <SwconfPrimivitveType> Тип принимаемого примитива
 * @param <Object_Type> Тип (де)сериализуемых объектов 
 */
public interface ISwconfSerializationProvider<SwconfPrimivitveType extends SwconfPrimitive, Object_Type> {

	/** Подходит ли для такого класса и {@link SwconfPrimitive}'а */
	public boolean isAccetps(Class<?> type);
	
	/** Десериализовать объект из Swconf */
	public Object_Type deserialize(Class<?> cl, Object_Type obj, SwconfPrimivitveType swconfObject);
	
	/** Сериализовать объект */
	public SwconfPrimivitveType serialize(Object_Type obj); 
	
	/** Создать новый экземпляр */
	public Object_Type createNewInstance(Class<?> classOfObject, SwconfPrimivitveType swconfObject);
	
}
