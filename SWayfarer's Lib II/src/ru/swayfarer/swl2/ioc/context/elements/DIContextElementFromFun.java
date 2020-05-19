package ru.swayfarer.swl2.ioc.context.elements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction0;

@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class DIContextElementFromFun implements IDIContextElement {

	public Class<?> associatedClass;
	public String name;
	public IFunction0<Object> objectCreationFun;

	public DIContextElementFromFun() {}
	
	@Override
	public Object getValue()
	{
		return objectCreationFun.apply();
	}
	
	@Override
	public String toString()
	{
		return "prototype from fun: " + objectCreationFun;
	}
}