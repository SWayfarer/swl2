package ru.swayfarer.swl2.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedMap;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.collections.streams.IDataStream;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.ioc.DIManager.DISwL;
import ru.swayfarer.swl2.ioc.componentscan.DISwlComponent;
import ru.swayfarer.swl2.ioc.componentscan.DISwlSource;
import ru.swayfarer.swl2.ioc.context.elements.ContextElementType;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;

public class DIAnnotation {

	public static ILogger logger = LoggingManager.getLogger();
	
	public static IExtendedMap<Class<?>, IFunction1<Annotation, String>> sourcesContextFun = CollectionsSWL.createExtendedMap();
	
	public static IExtendedMap<Class<?>, IFunction1<Annotation, String>> findsContextFun = CollectionsSWL.createExtendedMap();
	
	public static IExtendedMap<Class<?>, IFunction1<Annotation, String>> elementNameFuns = CollectionsSWL.createExtendedMap();
	public static IExtendedMap<Class<?>, IFunction1<Annotation, String>> elementContextFuns = CollectionsSWL.createExtendedMap();
	public static IExtendedMap<Class<?>, IFunction1<Annotation, Boolean>> elementUsingNameFuns = CollectionsSWL.createExtendedMap();
	public static IExtendedMap<Class<?>, IFunction1<Annotation, ContextElementType>> elementTypeFuns = CollectionsSWL.createExtendedMap();
	
	public static IExtendedMap<Class<?>, IFunction1<Annotation, String>> componentNameFuns = CollectionsSWL.createExtendedMap();
	public static IExtendedMap<Class<?>, IFunction1<Annotation, String>> componentContextFuns = CollectionsSWL.createExtendedMap();
	public static IExtendedMap<Class<?>, IFunction1<Annotation, Boolean>> componentUsingNameFuns = CollectionsSWL.createExtendedMap();
	public static IExtendedMap<Class<?>, IFunction1<Annotation, ContextElementType>> componentTypeFuns = CollectionsSWL.createExtendedMap();
	public static IExtendedMap<Class<?>, IFunction1<Annotation, Class<?>>> componentAssociatedClassFuns = CollectionsSWL.createExtendedMap();
	
	public static String getFindAnnotationContextName(Annotation annotation) 
	{
		return getByNamedFun(annotation, "context", String.class, findsContextFun, FindInContext.class);
	}
	
	public static String getSourceContextName(Annotation annotation) 
	{
		return getByNamedFun(annotation, "context", String.class, sourcesContextFun, DISwlSource.class);
	}
	
	public static ContextElementType getComponentType(Annotation annotation) 
	{
		return getByNamedFun(annotation, "type", ContextElementType.class, componentTypeFuns, DISwlComponent.class);
	}
	
	public static String getComponentName(Annotation annotation) 
	{
		return getByNamedFun(annotation, "name", String.class, componentNameFuns, DISwlComponent.class);
	}
	
	public static String getComponentContextName(Annotation annotation) 
	{
		return getByNamedFun(annotation, "context", String.class, componentContextFuns, DISwlComponent.class);
	}
	
	public static Class<?> getComponentAssociatedClass(Annotation annotation) 
	{
		return getByNamedFun(annotation, "associated", Class.class, componentAssociatedClassFuns, DISwlComponent.class);
	}
	
	public static boolean isComponentUsingName(Annotation annotation) 
	{
		return getByNamedFun(annotation, "usingName", boolean.class, componentUsingNameFuns, DISwlComponent.class);
	}
	
	public static ContextElementType getElementType(Annotation annotation) 
	{
		return getByNamedFun(annotation, "type", ContextElementType.class, elementTypeFuns);
	}
	
	public static String getElementName(Annotation annotation) 
	{
		return getByNamedFun(annotation, "name", String.class, elementNameFuns);
	}
	
	public static String getElementContextName(Annotation annotation) 
	{
		return getByNamedFun(annotation, "context", String.class, elementContextFuns);
	}
	
	public static boolean isElementUsingName(Annotation annotation) 
	{
		return getByNamedFun(annotation, "usingName", boolean.class, elementUsingNameFuns);
	}
	
	public static <T> T getByNamedFun(Annotation annotation, String paramName, Class<?> filterClass, IExtendedMap<Class<?>, IFunction1<Annotation, T>> elements) 
	{
		return getByNamedFun(annotation, paramName, filterClass, elements, DISwL.class);
	}
	
	public static <T> T getByNamedFun(Annotation annotation, String paramName, Class<?> filterClass, IExtendedMap<Class<?>, IFunction1<Annotation, T>> elements, Class<? extends Annotation> markerAnnotationCl) 
	{
		Class<? extends Annotation> annotationClass = annotation.getClass();
		
		IFunction1<Annotation, T> fun = elements.get(annotationClass);
		
		if (fun == null)
		{
			fun = createNamedFun(paramName, annotation, filterClass, markerAnnotationCl);
			elements.put(annotationClass, fun);
		}
		
		return fun.apply(annotation);
	}
	
	public static <Ret_Type> IFunction1<Annotation, Ret_Type> createNamedFun(String name, Annotation annotation, Class<?> retTypeFilter, Class<? extends Annotation> markerAnnotationCl)
	{
		Class<? extends Annotation> annotationType = annotation.annotationType();
		
		Method nameMethod = ReflectionUtils.methods(annotationType)
			.named(name)
			.noArgs()
			.returnType(retTypeFilter)
		.first();
		
		if (nameMethod != null)
			return (a) -> ExceptionsUtils.safeReturn(() -> (Ret_Type) nameMethod.invoke(a), null);
		
		Annotation diswl = ReflectionUtils.findAnnotationRec(annotationType, markerAnnotationCl);
		
		if (diswl != null)
			return (a) -> ReflectionUtils.invokeMethod(diswl, name).getResult();
		
		return null;
	}
	
	public static Annotation findDIAnnotation(Class<?> cl)
	{
		return findByDISwl(cl.getAnnotations());
	}
	
	public static Annotation findDIAnnotation(Method method)
	{
		return findByDISwl(method.getAnnotations());
	}
	
	public static Annotation findDIAnnotation(Parameter param)
	{
		return findByDISwl(param.getAnnotations());
	}

	public static Annotation findDIAnnotation(Field field)
	{
		return findByDISwl(field.getAnnotations());
	}
	
	public static Annotation findDIComponentAnnotation(Class<?> cl)
	{
		return findByAnnotation(cl, DISwlComponent.class);
	}
	
	public static Annotation findDISourceAnnotation(Class<?> cl)
	{
		return findByAnnotation(cl, DISwlSource.class);
	}
	
	public static Annotation findByAnnotation(Class<?> cl, Class<? extends Annotation> classOfAnnotation)
	{
		return DataStream.of(cl.getAnnotations()).first((annotation) -> annotation.annotationType().equals(classOfAnnotation) || ReflectionUtils.findAnnotationRec(annotation.annotationType(), classOfAnnotation) != null);
	}
	
	public static Annotation findByDISwl(Annotation[] annotations)
	{
		return findByDISwl(DataStream.of(annotations));
	}
	
	public static Annotation findByDISwl(IDataStream<Annotation> annotations)
	{
		return annotations.first((annotation) -> {
			return annotation.annotationType().equals(DISwL.class) || ReflectionUtils.findAnnotationRec(annotation.annotationType(), DISwL.class) != null;
		});
	}
}
