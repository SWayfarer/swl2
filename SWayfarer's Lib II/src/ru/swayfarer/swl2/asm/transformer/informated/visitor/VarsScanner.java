package ru.swayfarer.swl2.asm.transformer.informated.visitor;

import ru.swayfarer.swl2.asm.informated.AnnotationInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo;
import ru.swayfarer.swl2.asm.informated.MethodInfo.LocalVarEntry;
import ru.swayfarer.swl2.asm.informated.VariableInfo;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.AnnotationVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Label;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;

/**
 * Сканнер переменных, который создает {@link VariableInfo} для целевого метода 
 * @author swayfarer
 *
 */
public class VarsScanner extends MethodVisitor{
	
	/** Кол-во переменных, которые являются параметрами */
	public int paramsCount;
	
	/** Информация о методе, в который добавляются переменные */
	@InternalElement
	public MethodInfo info;
	
	/** Статический ли метод? */
	public boolean isStatic;
	
	/** Индекс следующего параметра */
	public int nextParamIndex;
	
	public VarsScanner(MethodInfo info, MethodVisitor mv, int paramsCount, boolean isStatic)
	{
		super(ASM7, mv);
		this.info = info;
		this.paramsCount = paramsCount;
		this.isStatic = isStatic;
		
		info.paramsCount = paramsCount;
	}
	
	@Override
	public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index)
	{
		VariableInfo info = new VariableInfo(index, name, descriptor, signature);
		
		this.info.localIdToName.add(new LocalVarEntry(index, name)); 
		
		if (isThisVar())
		{
			nextParamIndex ++;
		}
		else if (isNextLocalVarParam())
		{
			this.info.addParam(nextParamIndex, info);
			nextParamIndex ++;
		}
		
		this.info.addVariable(info);
	}
	
	/** 
	 * Является ли переменная на стеке this? 
	 * <br> (0 для не-статических методов, для статики не существует) 
	 */
	public boolean isThisVar()
	{
		return !isStatic && nextParamIndex == 0;
	}
	
	/**
	 * Является ли следующая переменная параметром?
	 */
	public boolean isNextLocalVarParam()
	{
		if (isStatic)
			return nextParamIndex < paramsCount;
		
		return nextParamIndex <= paramsCount;
	}
	
	public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible, AnnotationVisitor av)
	{
		AnnotationInfo info = new AnnotationInfo();
		info.descritor = descriptor;
		
		this.info.cacheAnnotation(isStatic ? parameter : parameter + 1, info);
			
		AnnotationScanner scanner = new AnnotationScanner(info, av);
		
		return scanner;
	}
	
	@Override
	public void visitMaxs(int maxStack, int maxLocals)
	{
		this.info.maxStack = maxStack;
		this.info.maxLocals = maxLocals;
	}

}
