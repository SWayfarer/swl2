package ru.swayfarer.swl2.asm.transfomer.injection;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

/** Иньекция метода */
public interface IMethodInjection {

	/** Получить имя иньектируемого метода */
	public String getInjectedMethodName();
	
	/** Получить дескриптор иньектируемого метода */
	public String getInjectedMethodDesc();
	
	/** Получить внутреннее имя класса, в котором находится иньектируемый метод */
	public String getInjectedClassInternalName();
	
	/** Получить начало дескриптора целевого метода */
	public String getTargetMethodDesc();
	
	/** Получить имя целевого метода */
	public String getTargetMethodName();
	
	/** Получить внутреннее имя класса, в котором находится целевой метод */
	public String getTargetClassCanonicalName();
	
	/** Иньектировать */
	public boolean inject(ClassInfo targetClassInfo, MethodInfo targetMethodInfo, boolean atEnd, AdviceAdapter methodVisitor);
}
