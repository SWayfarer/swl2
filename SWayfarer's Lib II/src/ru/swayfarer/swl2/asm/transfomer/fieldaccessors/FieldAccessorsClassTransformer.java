package ru.swayfarer.swl2.asm.transfomer.fieldaccessors;

import java.util.Map;

import ru.swayfarer.swl2.asm.informated.ClassInfo;
import ru.swayfarer.swl2.asm.informated.FieldInfo;
import ru.swayfarer.swl2.asm.transformer.informated.InformatedClassTransformer;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassReader;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;

@SuppressWarnings("unchecked")
public class FieldAccessorsClassTransformer extends InformatedClassTransformer {

	public IFunction1<FieldInfo, String> fieldsSetterNameFun = (field) -> "internal_set" + field.name;
	public IFunction1<FieldInfo, String> fieldsGetterNameFun = (field) -> "internal_get" + field.name;;
	
	public Map<String, IExtendedList<String>> registeredFields = CollectionsSWL.createConcurrentHashMap();
	
	@Override
	public void transform(String name, byte[] bytes, ClassReader reader, ClassWriter writer, ClassInfo info)
	{
		ClassVisitor cv = writer;
		
		cv = new FieldAccessorClassVisitor(cv, info, this);
		
		acceptCV(reader, bytes, cv);
	}
	
	public <T extends FieldAccessorsClassTransformer> T setFieldsSetterNameFun(IFunction1<FieldInfo, String> fun)
	{
		this.fieldsSetterNameFun = fun;
		return (T) this;
	}
	
	public <T extends FieldAccessorsClassTransformer> T setFieldsGetterNameFun(IFunction1<FieldInfo, String> fun)
	{
		this.fieldsGetterNameFun = fun;
		return (T) this;
	}
	
	public boolean isMustProcessField(String classInternalName, String fieldName)
	{
		IExtendedList<String> fields = getFieldsFor(classInternalName, false);
		
		if (CollectionsSWL.isNullOrEmpty(fields))
			return false;
		
		return fields.contains(fieldName);
	}
	
	public <T extends FieldAccessorsClassTransformer> T registerField(FieldInfo fieldInfo)
	{
		return registerField(fieldInfo.getOwner().getCanonicalName(), fieldInfo.name);
	}
	
	public boolean isRegisteredToReplace(FieldInfo fieldInfo)
	{
		return isRegisteredToReplace(fieldInfo.getOwner().getInternalName(), fieldInfo.name);
	}
	
	public boolean isRegisteredToReplace(String ownerInternalName, String name)
	{
		IExtendedList<String> fields = getFieldsFor(ownerInternalName, false);
		
		if (CollectionsSWL.isNullOrEmpty(fields))
			return false;
		
		return fields.contains(name);
	}
	
	public <T extends FieldAccessorsClassTransformer> T registerField(String classCanonicalName, String fieldname)
	{
		String classInternalName = classCanonicalName.replace(".", "/");
		IExtendedList<String> fields = getFieldsFor(classInternalName, true);
		fields.addExclusive(fieldname);
		return (T) this;
	}
	
	public <T extends FieldAccessorsClassTransformer> T registerField(Class<?> cl, String fieldname)
	{
		return registerField(cl.getName(), fieldname);
	}
	
	public IExtendedList<String> getFieldsFor(String classInternalName, boolean force)
	{
		IExtendedList<String> fields = registeredFields.get(classInternalName);
		
		if (fields == null && force)
		{
			fields = CollectionsSWL.createExtendedList();
			registeredFields.put(classInternalName, fields);
		}
		
		return fields;
	}
	
}
