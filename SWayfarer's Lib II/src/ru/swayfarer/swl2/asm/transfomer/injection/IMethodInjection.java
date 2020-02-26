package ru.swayfarer.swl2.asm.transfomer.injection;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

public interface IMethodInjection {

	public String getInjectedMethodName();
	public String getInjectedMethodDesc();
	public String getInjectedClassInternalName();
	
	public String getTargetMethodDesc();
	public String getTargetMethodName();
	public String getTargetClassInternalName();
	
	public boolean inject(ClassInfo targetClassInfo, MethodInfo targetMethodInfo, boolean atEnd, AdviceAdapter methodVisitor);
}
