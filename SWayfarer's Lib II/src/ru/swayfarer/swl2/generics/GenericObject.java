package ru.swayfarer.swl2.generics;

import java.lang.reflect.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.var;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Data
public class GenericObject {

	@Builder.Default
	public IExtendedList<GenericObject> childs = CollectionsSWL.createExtendedList();
	
	@Builder.Default
	public String typeCanonicalName = "<empty>";
	
	public boolean isClassPresented()
	{
		try
		{
			loadClass();
			return true;
		}
		catch (Throwable e)
		{
			
		}
		
		return false;
	}
	
	public boolean hasChildren()
	{
		return !CollectionsSWL.isNullOrEmpty(childs);
	}
	
	public Class<?> loadClass() throws ClassNotFoundException
	{
		return Class.forName(typeCanonicalName);
	}
	
	public Class<?> loadClassSafe()
	{
		return ReflectionUtils.findClass(typeCanonicalName);
	}
	
	public String toString(int indent)
	{
		DynamicString ret = new DynamicString();
		
		String indentStr = StringUtils.createSpacesSeq(4 * indent);
		
		ret.append("\n");
		ret.append(indentStr);
		ret.append(typeCanonicalName);
		
		if (!CollectionsSWL.isNullOrEmpty(childs))
		{
			ret.append("{");
			
			for (var child : childs)
			{
				ret.append(child.toString(indent + 1));
			}

			
			ret.append("\n");
			ret.append(indentStr);
			ret.append("}");
		}
		
		return ret.toString();
	}
	
	public String toString()
	{
		return "\n" + toString(0);
	}
	
	public static IExtendedList<GenericObject> ofField(Field field)
	{
		var parser = new GenericsParser();
		parser.parse(field.getGenericType().toString());
		var firstElement = parser.result.getFirstElement();
		
		return firstElement == null ? null : firstElement.getChilds();
	}
}
