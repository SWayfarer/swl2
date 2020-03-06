package ru.swayfarer.swl2.reference;

/**
 * Ссылка на элемент 
 * @author swayfarer
 */
public class ParameterizedReference<Element_Type> extends AbstractReference<Element_Type> {

	/** Конструктор */
	public ParameterizedReference()
	{
		super();
	}

	/** Конструктор */
	public ParameterizedReference(Element_Type initialValue)
	{
		super(initialValue);
	}

}
