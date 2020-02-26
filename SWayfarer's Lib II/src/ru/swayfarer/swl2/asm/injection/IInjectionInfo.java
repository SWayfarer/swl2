package ru.swayfarer.swl2.asm.injection;

import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Type;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.commons.AdviceAdapter;

/**
 * Информация о вставке
 * @author swayfarer
 */
public interface IInjectionInfo {

	/** 
	 * Применить в MethodVisitor вставку
	 * @param mv AdviceAdapter
	 */
	public void inject(AdviceAdapter mv, int opcode);
	
	/**
	 * Применять ли на выходе из метода? 
	 */
	public boolean isInjectOnExit();
	
	/**
	 * Цель 
	 * @return
	 */
	public String getTargetClass();
	public String getTargetMethod();
	public String getTargetDescriptor();
	public void setTargetClassReturnType(Type type);
	public void setTargetClassStatic(boolean isStatic);
	
}
