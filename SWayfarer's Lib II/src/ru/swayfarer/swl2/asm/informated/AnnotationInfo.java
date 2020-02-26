package ru.swayfarer.swl2.asm.informated;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * Информация об аннотации
 * @author swayfarer
 */
@Data
@SuppressWarnings("unchecked")
public class AnnotationInfo {

	public AnnotationInfo() {}
	
	public AnnotationInfo(String descritor)
	{
		super();
		this.descritor = descritor;
	}

	/**
	 * Дескриптор аннотации
	 */
	public String descritor;
	
	/**
	 * Параметры аннотации, заданные в ней. <h1>Пример:</h1> @InjectionAsm(injectOnExit = true) будет иметь параметры Map = {injectOnExit=true}
	 */
	public Map<String, Object> params = new HashMap<>();
	
	@Override
	public String toString()
	{
		return "AnnotationInfo [descritor=" + descritor + ", params=" + params + "]";
	}
	
	public <Param_Type> Param_Type getParam(String name)
	{
		return (Param_Type) params.get(name);
	}
	
	public <Param_Type> Param_Type getParam(String name, Param_Type defaultValue)
	{
		Param_Type param = getParam(name);
		
		return param == null ? defaultValue : param;
	}
}
