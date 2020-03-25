package ru.swayfarer.swl2.xml;

import java.util.Map;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.StringUtils;

@SuppressWarnings("unchecked")
public class XmlTag {

	/** Родительский таг */
	@InternalElement
	public XmlTag parent;
	
	/** Дочерние таги */
	@InternalElement
	public IExtendedList<XmlTag> childrenTags = CollectionsSWL.createExtendedList();
	
	/** Атрибуты тага */
	@InternalElement
	public Map<String, XmlAttribute> attributes = CollectionsSWL.createHashMap();
	
	/** Имя тага */
	@InternalElement
	public String name;
	
	/** Контент тага */
	@InternalElement
	public String content;
	
	/** Конструктор без указания родителя */
	public XmlTag() {}
	
	/** Конструктор с указанием родителя */
	public XmlTag(XmlTag parent)
	{
		this.parent = parent;
		
		if (parent != null)
			parent.childrenTags.add(this);
	}
	
	/** Есть ли атрибут с таким именем? */
	public boolean hasAttribute(@ConcattedString Object... name)
	{
		String s = StringUtils.concat(name);
		return attributes.containsKey(s);
	}
	
	/** Есть ли дочерний элемент с таким именем? */
	public boolean hasChild(@ConcattedString Object... text)
	{
		return firstChild(text) != null;
	}
	
	/** Выполнить функцию для каждого тага внутри этого, включая сам таг */
	public void forEachChild(IFunction1NoR<XmlTag> fun)
	{
		forEachChild(fun, true);
	}
	
	/** Выполнить функцию для каждого тага внутри этого, включая сам таг */
	public void forEachChild(IFunction1NoR<XmlTag> fun, boolean isRecurcive)
	{
		forEachChild(this, fun, isRecurcive);
	}
	
	/** Выполнить функцию для каждого тага внутри этого, включая сам таг */
	public static void forEachChild(XmlTag tag, IFunction1NoR<XmlTag> fun, boolean isRecurcive)
	{
		fun.apply(tag);
		if (isRecurcive)
			tag.childrenTags.each((e) -> forEachChild(e, fun, isRecurcive));
		else
			tag.childrenTags.each(fun::apply);
	}
	
	/** Получить все дочерние элементы с таким именем */
	public IExtendedList<? extends XmlTag> childs(@ConcattedString Object... name)
	{
		String str = StringUtils.concat(name);
		return childrenTags.dataStream().filter((e) -> e.name.equals(str)).toList();
	}
	
	/** Получить первый дочерний элемент с таким именем, если он существует */
	public <T extends XmlTag> T firstChild(@ConcattedString Object... name)
	{
		String str = StringUtils.concat(name);
		return (T) childrenTags.dataStream().find((e) -> e.name.equals(str));
	}
	
	/** Получить xml-строку */
	public String toXmlString()
	{
		StringBuilder builder = new StringBuilder();
		toXmlString(builder, 0);
		return builder.toString();
	}
	
	/** Получить атрибут как double */
	public Number getNumberAttribute(@ConcattedString Object... name)
	{
		XmlAttribute attr = getAttribute(name);
		return attr == null ? null : attr.getNumberValue();
	}
	
	/** Получить атрибут как строку */
	public String getStringAttribute(@ConcattedString Object... name)
	{
		XmlAttribute attr = getAttribute(name);
		return attr == null ? null : attr.getValue();
	}
	
	/** Получить атрибут тага */
	public XmlAttribute getAttribute(@ConcattedString Object... name)
	{
		String s = StringUtils.concat(name);
		return attributes.get(s);
	}
	
	/** Записать xml-строку в билдер */
	@InternalElement
	public void toXmlString(StringBuilder builder, int indient)
	{
		String indientSeq = StringUtils.createSpacesSeq(indient * 4);
		
		builder.append(indientSeq);
		builder.append("<");
		builder.append(name);
		
		for (Map.Entry<String, XmlAttribute> attr : attributes.entrySet())
		{
			builder.append(" ");
			
			builder.append(attr.getKey());
			builder.append(" = \"");
			builder.append(attr.getValue().value);
			builder.append("\"");
		}
		
		boolean isSelfClosed = false;
		
		if (childrenTags.isEmpty() && StringUtils.isEmpty(content))
		{
			builder.append("/>");
			isSelfClosed = true;
		}
		else
			builder.append(">");
		
		if (!childrenTags.isEmpty())
		{
			for (XmlTag child : childrenTags)
			{
				builder.append("\n");
				child.toXmlString(builder, indient + 1);
			}
			
			builder.append("\n");
		}
		
		if (!isSelfClosed)
		{
			builder.append(indientSeq);
			builder.append("</");
			builder.append(name);
			builder.append(">");
		}
	}
	
	@Override
	public String toString()
	{
		return "XmlTag [childrenTags=" + childrenTags + ", attributes=" + attributes + ", name=" + name + ", content=" + content + "]";
	}
	
}
