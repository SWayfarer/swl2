package ru.swayfarer.swl2.asm.transfomer.injection;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.observable.events.AbstractCancelableEvent;

/**
 * Событие создания иньекции
 * @author swayfarer
 */
@AllArgsConstructor @Data
public class InjectionCreationEvent extends AbstractCancelableEvent {

	/** Иньекция, которая была создана */
	public MethodInjection methodInjection;
	
	/** Информация о классе, из которого будет браться метод-иньекция */
	public ClassInfo injectionClassInfo;
	
}
