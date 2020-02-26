package ru.swayfarer.swl2.asm.transformer.accesstransformer.visitor;

import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes;

public class AccessTypes implements Opcodes{
	
	public static boolean isInterface(int i)
	{
		return (i & ACC_INTERFACE) != 0;
	}
	
	public static boolean isFinal(int i)
	{
		return (i & ACC_FINAL) != 0;
	}
	
	public static boolean isProtected(int i)
	{
		return (i & ACC_PROTECTED) != 0;
	}
	
	public static boolean isPrivate(int i)
	{
		return (i & ACC_PRIVATE) != 0;
	}
	
	public static int openAccess(int access) {
		
		if (isFinal(access))
		{
			access = access ^ ACC_FINAL;
		}
		
		if (isPrivate(access))
		{
			access = (access ^ ACC_PRIVATE) | ACC_PUBLIC;
		}
		
		if (isProtected(access))
		{
			access = (access ^ ACC_PROTECTED) | ACC_PUBLIC;
		}
		
		return access;
		
	}

}
