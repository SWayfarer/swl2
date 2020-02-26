package ru.swayfarer.swl2.asm.transfomer.injection;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.markers.InternalElement;

@Data
@AllArgsConstructor
public class ClassInjectionInfo {

	@InternalElement
	/** Имя класса */
	public String classNormalName;
	
	@InternalElement
	/** Лист иньекций в методы этого класса */
	public IExtendedList<IMethodInjection> methodInjections = CollectionsSWL.createExtendedList();
	
	@InternalElement
	/** Конструктор для сериализации */
	public ClassInjectionInfo() { }
	
	/** Получить иньекции в метод с указанными именем и дескриптором */
	public IExtendedList<IMethodInjection> getMethodInjections(String methodName, String methodDesc)
	{
		return methodInjections.dataStream()
				.filter((method) -> methodDesc.startsWith(method.getTargetMethodDesc()))
				.toList();
	}
}
