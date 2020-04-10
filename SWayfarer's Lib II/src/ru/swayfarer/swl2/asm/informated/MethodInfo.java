package ru.swayfarer.swl2.asm.informated;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.ToString;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;

/**
 * Информация о методе
 * @author swayfarer
 */
@Data
@ToString
public class MethodInfo {

	/** Доступ к методу через опкоды */
	public int access;
	
	/** Класс, в котором находится метод */
	public ClassInfo owner;
	
	public int paramsCount;
	
	/** Имя метода */
	public String name;
	
	/** Сигнатура метода */
	public String signature;
	
	/** Дескриптор метода */
	public String descriptor;
	
	/** Exception'ы, которые кидает метод (то, что после throws) */
	public IExtendedList<String> exceptions = CollectionsSWL.createExtendedList();
	
	/** Типы параметров метода в дескрипторах */
	protected IExtendedList<String> paramsTypes;

	/** Параметры метода */
	public IExtendedList<VariableInfo> parameters = CollectionsSWL.createExtendedList();
	
	/** Карта, переводящая id переменной в ее имя */
	public Map<Integer, String> localIdToName = CollectionsSWL.createHashMap();
	
	/** Локальные переменные метода */
	public IExtendedList<VariableInfo> localVars = CollectionsSWL.createExtendedList();
	
	/** Аннотации метода */
	public IExtendedList<AnnotationInfo> annotations = CollectionsSWL.createExtendedList();
	
	/** Максимальный размер стека */
	public int maxStack;
	
	/** Максимальное кол-во локальных переменных */
	public int maxLocals;
	
	/** Ассоциация аннотации с номером локальной переменной */
	public Map<Integer, IExtendedList<AnnotationInfo>> varToAnnotation = CollectionsSWL.createHashMap(); 
	
	/**
	 * Получить информацию о переменной по ее id
	 * @param id Id переменной 
	 * @return Информация о переменной. Null, если нет такой переменной.
	 */
	public VariableInfo getVariable(int id)
	{
		return id >= 0 && localVars.size() > id ? localVars.get(id) : null;
	}
	
	/** Получить параметры метода */
	public IExtendedList<VariableInfo> getParams()
	{
		return parameters.copy();
	}
	
	public List<String> getParamsTypes()
	{
		if (paramsTypes == null)
		{
			paramsTypes = CollectionsSWL.createExtendedList();
			
			for (VariableInfo info : parameters)
			{
				paramsTypes.add(info.descriptor);
			}
		}
		return paramsTypes;
	}
	
	/** Метод не имеет параметров? */
	public boolean isHasNoParameters()
	{
		return getParametersCount() == 0;
	}
	
	/** Является ли метод void'ом? */
	public boolean isVoid()
	{
		return Type.VOID_TYPE.equals(getReturnType());
	}
	
	/**
	 * Получить информацию о переменной по ее имени
	 * @param name Имя переменной
	 * @return Информация о переменной. Null, если нет такой переменной.
	 */
	public VariableInfo getVariable(String name)
	{
		for (VariableInfo info : localVars)
		{
			if (info.name.equals(name))
				return info;
		}
		
		return null;
	}
	
	/**
	 * Имеет ли класс аннотацию?
	 * @param desc Дескриптор аннотации
	 * @return Имеет ли?
	 */
	public boolean hasAnnotation(String desc)
	{
		return getFirstAnnotation(desc) != null;
	}
	
	/**
	 * Получить все аннотации с по дескриптору
	 * @param desc Дескриптор аннотации
	 * @return Лист аннотаций на классе
	 */
	public List<AnnotationInfo> getAnnotationList(String desc)
	{
		List<AnnotationInfo> ret = CollectionsSWL.createExtendedList();
		
		for (AnnotationInfo info : annotations)
			if (info.descritor.equals(desc))
				ret.add(info);
		
		return ret;
	}
	
	/**
	 * Получить первую аннотацию по дескриптору
	 * @param desc Дескриптор аннотации
	 * @return Первая аннотация. Null, если таковой нет
	 */
	public AnnotationInfo getFirstAnnotation(String desc)
	{
		for (AnnotationInfo info : annotations)
			if (info.descritor.equals(desc))
				return info;
		
		return null;
	}
	
	/**
	 * Получить кешированные аннотации для переменной 
	 * @param id Id переменной
	 * @return Лист кешированных аннотаций
	 */
	public List<AnnotationInfo> getCachedAnnotations(int id)
	{
		IExtendedList<AnnotationInfo> ret = varToAnnotation.get(id);
		
		if (ret == null)
		{
			ret = CollectionsSWL.createExtendedList();
			
			varToAnnotation.put(id, ret);
		}
		
		return ret;
	}
	
	/**
	 * Добавить параметер метода 
	 */
	public void addParam(int id, VariableInfo info)
	{
		if (varToAnnotation.containsKey(id))
		{
			info.annotations.addAll(getCachedAnnotations(id));
			varToAnnotation.remove(id);
		}
		
		parameters.add(info);
	}
	
	/** 
	 * Кешировать аннотацию
	 * @param id Id переменной 
	 * @param info Информация об аннотации
	 */
	public void cacheAnnotation(int id, AnnotationInfo info)
	{
		getCachedAnnotations(id).add(info);
	}
	
	/** Получить дескриптор возвращаемого типа */
	public String getReturnDesc()
	{
		return descriptor.substring(descriptor.indexOf(")")+1);
	}
	
	/** Получить возвращаемое значение */
	public Type getReturnType()
	{
		return Type.getType(getReturnDesc());
	}
	
	/**
	 * Добавить переменную 
	 * @param info Информация о переменной, которую добавляем
	 */
	public void addVariable(VariableInfo info)
	{
		localVars.add(info);
	}
	
	/** Получить кол-во параметров метода */
	public int getParametersCount()
	{
		return paramsCount;
	}
	
	/** Является ли метод статическим? */
	public boolean isStatic()
	{
		return (access & Opcodes.ACC_STATIC) != 0;
	}
}
