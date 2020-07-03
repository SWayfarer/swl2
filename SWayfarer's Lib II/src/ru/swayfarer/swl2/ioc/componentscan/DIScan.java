package ru.swayfarer.swl2.ioc.componentscan;

import java.util.concurrent.atomic.AtomicBoolean;

import ru.swayfarer.swl2.asm.classfinder.ClassFinder;
import ru.swayfarer.swl2.markers.InternalElement;

/**
 * Сканнер DI-элементов внутри пакетов
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class DIScan {

	/** Было ли уже совершено сканирование? */
	@InternalElement
	public AtomicBoolean isAlreadyScanned = new AtomicBoolean(false);
	
	/** Искалка классов, которые будут просканированы */
	@InternalElement
	public ClassFinder classFinder = new ClassFinder();
	
	/** Сканнер компонентов ({@link DISwlComponent}) */
	@InternalElement
	public ComponentScan componentScan = new ComponentScan(classFinder).registerDefaultCreationFuns();
	
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
		return scan(pkg, false);
	}
	
	/**
	 * Сканировать пакет 
	 * @param pkg Сканируемый пакет
	 * @param force Пропустить ли проверку на {@link #isAlreadyScanned}?
	 * @return Этот объкет (this)
	 */
	public <T extends DIScan> T scan(String pkg, boolean force)
	{
		if (!force && isAlreadyScanned.get())
			return (T) this;
		
		isAlreadyScanned.set(true);
		
		classFinder.scan(pkg);
		return (T) this;
	}
	
	public <T extends DIScan> T reset()
	{
		isAlreadyScanned.set(false);
		return (T) this;
	}
}
