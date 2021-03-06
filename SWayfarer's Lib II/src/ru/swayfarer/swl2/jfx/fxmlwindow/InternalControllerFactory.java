package ru.swayfarer.swl2.jfx.fxmlwindow;

import java.util.Map;
import java.util.UUID;

import javafx.fxml.FXML;
import ru.swayfarer.swl2.asm.BytesClassLoader;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.AnnotationVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.ClassWriter;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.FieldVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Label;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.MethodVisitor;
import ru.swayfarer.swl2.z.dependencies.org.objectweb.asm.Opcodes;

/**
 * Фабрика по производству контроллеров, которые будут переардесованы в {@link FxmlWindow} 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class InternalControllerFactory implements Opcodes {

	/** Логгер */
	@InternalElement
	public static ILogger logger = LoggingManager.getLogger();
	
	/** Карта с кэшированными котроллерами по ссылкам на их fxml'ки */
	@InternalElement
	public static Map<String, Class<?>> generatedControllers = CollectionsSWL.createHashMap();
	
	/** Регулярка, по которой находятся все именнованные элементы в fxml */
	@InternalElement
	public static String FXML_NAMED_ELEMENT_REGEX = StringUtils.regex()
			.text("fx:id=\"")
			.some()
			.raw("[^\"]")
			.text("\"")
	.build();
	
	/** Функция, которая генерирует контроллеры относительно ссылки на fxml */
	public static IFunction1<String, Object> fxmlControllerFactoryFun = new AutogeneratedControllerFactoryFun();
	
	/** Генерация контроллера для fxml'ки по ссылке */
	public static <T> T generateController(ResourceLink fxmlRlink)
	{
		String rlink = fxmlRlink.toRlinkString();
		
		// Если класс для контроллера уже хотя бы раз создавался, то просто берем его из кэша 
		Class<?> cl = generatedControllers.get(rlink);
		
		if (cl != null)
			return ReflectionUtils.newInstanceOf(cl);
		
		// Или обращаемся к функции-генератору
		return (T) fxmlControllerFactoryFun.apply(rlink);
	}
	
	/**
	 * Информация об fxml-файле
	 * @author swayfarer
	 *
	 */
	public static class FxmlFileInfo {
		
		/** Именованные элементы (имеющие id) */
		public IExtendedList<String> namedElements = CollectionsSWL.createExtendedList();
		
		/** Получить значение информации по ссылке с указанной кодировкой */
		public static FxmlFileInfo valueOf(String fxmlRlink, String encoding)
		{
			ResourceLink rlink = RLUtils.createLink(fxmlRlink);
			
			if (!RLUtils.exists(rlink))
				return null;
			
			String str = rlink.toStream().readAllAsString();
			
			IExtendedList<String> namedElements = StringUtils.getAllMatches(FXML_NAMED_ELEMENT_REGEX, str).dataStream()
				.mapped((namedElement) -> StringUtils.subString(7, -1, namedElement))
			.toList();
			
			FxmlFileInfo fileInfo = new FxmlFileInfo();
			
			fileInfo.namedElements = namedElements;
			
			return fileInfo;
		}
		
	}
	
	/**
	 * Функция, генерирующая классы-контроллеры по ключевым словам в fxml'ке
	 * @author swayfarer
	 */
	public static class AutogeneratedControllerFactoryFun implements IFunction1<String, Object> {

		@Override
		public Object apply(String fxml)
		{
			String generatedClassName = "GeneratedController_" + getRandomString() + getRandomString();
			FxmlFileInfo file = FxmlFileInfo.valueOf(fxml, "UTF-8");
			
			ExceptionsUtils.IfNull(file, IllegalStateException.class, "Fxml info for", fxml, "can't be null!");
			
			ClassWriter cv = new ClassWriter(0);
			
			// Генерация класса для нового контроллера 
			cv.visit(V1_8, ACC_PUBLIC | ACC_SUPER, generatedClassName, null, "java/lang/Object", null);
			{
				cv.visitSource("Generated", null);
				
				// Генерация полей 
				file.namedElements.dataStream()
					.distinct()
					.each((element) -> addField(element, cv));
				
				// Генерация дефолтного конструктора 
				addInit(cv, generatedClassName);
			}
			cv.visitEnd();
			
			// Загрузка сгенерированного класса 
			
			byte[] classBytes = cv.toByteArray();
			
			Class<?> controllerGeneratedClass = BytesClassLoader.loadClass(generatedClassName, classBytes);
			
			synchronized (generatedControllers)
			{
				generatedControllers.put(fxml, controllerGeneratedClass);
			}
			
			Object obj = ReflectionUtils.newInstanceOf(controllerGeneratedClass);
			
			return obj;
		}
		
		/** Получить рандомную строку */
		@InternalElement
		public String getRandomString()
		{
			return UUID.randomUUID().toString().replace("-", "");
		}
		
		/** Добавить конструктор */
		@InternalElement
		public void addInit(ClassVisitor cv, String className)
		{
			MethodVisitor methodVisitor = cv.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			methodVisitor.visitCode();
			Label label0 = new Label();
			methodVisitor.visitLabel(label0);
			methodVisitor.visitLineNumber(29, label0);
			methodVisitor.visitVarInsn(ALOAD, 0);
			methodVisitor.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
			methodVisitor.visitInsn(RETURN);
			Label label1 = new Label();
			methodVisitor.visitLabel(label1);
			methodVisitor.visitLocalVariable("this", "L" + className + ";", null, label0, label1, 0);
			methodVisitor.visitMaxs(1, 1);
			methodVisitor.visitEnd();
		}
		
		/** Добавить поле с указанным именем, отмеченное аннотацией {@link FXML} */
		@InternalElement
		public void addField(String name, ClassVisitor cv)
		{
			FieldVisitor fieldVisitor = cv.visitField(ACC_PUBLIC, name, "Ljava/lang/Object;", null, null);
			fieldVisitor.visitEnd();
			
			AnnotationVisitor annotationVisitor0 = fieldVisitor.visitAnnotation("Ljavafx/fxml/FXML;", true);
			annotationVisitor0.visitEnd();
		}
		
	}
	
}
