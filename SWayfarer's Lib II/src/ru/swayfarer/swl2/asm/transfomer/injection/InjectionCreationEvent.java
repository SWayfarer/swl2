package ru.swayfarer.swl2.asm.transfomer.injection;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.observable.events.AbstractCancelableEvent;

@AllArgsConstructor @Data
public class InjectionCreationEvent extends AbstractCancelableEvent {

	public MethodInjection methodInjection;
	public ClassInfo injectionClassInfo;
	
}
