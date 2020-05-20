package ru.swayfarer.swl2.ioc.componentscan;

import ru.swayfarer.swl2.asm.classfinder.ClassFinder;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Сканнер DI-элементов внутри пакетов
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class DIScan {

	/** Искалка классов, которые будут просканированы */
	@InternalElement
	public ClassFinder classFinder = new ClassFinder();
	
	/** Сканнер компонентов ({@link DISwlComponent}) */
	@InternalElement
	public ComponentScan componentScan = new ComponentScan(classFinder);
	
	/** Сканнер источников контекста ({@link DISwlSource})*/
	@InternalElement
	public ContextSourcesScan contextSourcesScan = new ContextSourcesScan(classFinder);
	
	/**
	 * Сканировать пакет 
	 * @param pkg Сканируемый пакет
	 * @return Этот объкет (this)
	 */
	public <T extends DIScan> T scan(String pkg)
	{
		classFinder.scan(pkg);
		return (T) this;
	}
}
