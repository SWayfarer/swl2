package ru.swayfarer.swl2.swconf.serialization;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.ToString;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.serialization.comments.CommentSwconf;
import ru.swayfarer.swl2.swconf.serialization.formatter.PrettyFormattedWriter;
import ru.swayfarer.swl2.swconf.serialization.formatter.PrettySwconfFormatter;
import ru.swayfarer.swl2.swconf.serialization.reader.SwconfReader;
import ru.swayfarer.swl2.swconf.serialization.writer.ISwconfWriter;
import ru.swayfarer.swl2.swconf.serialization.writer.SwconfWriter;

public class Test {

	public static void main(String[] args)
	{
		
		String swconf = "/** 123 */r = 4, d = '434343', name = {qwe = '323323', e = 5}, arr = [1, 2, 3, 4, 5, 6]";
		
		SwconfReader reader = new SwconfReader();
		ISwconfWriter writer = new SwconfWriter();
		SwconfSerialization serialization = new SwconfSerialization();
		
		TestClass testClass = new TestClass();
		testClass.testClass2.someBoolean = true;
		
		testClass.testClass2.booleans.add(false);
		
		SwconfObject object = serialization.serialize(testClass);
		
		TestClass class1 = serialization.deserialize(TestClass.class, object);
		
		writer = PrettyFormattedWriter.of(writer);
		
		writer.startWriting();
		writer.write(object);
		writer.endWriting();
		
		System.out.println(writer.toSwconfString());
	}
	
	@ToString
	public static class TestClass2 {
		
		public boolean someBoolean = false;
		
		public List<Boolean> booleans = CollectionsSWL.createExtendedList(true, true, true);
		
	}
	
	public static class Voodoo {
	    public static String chill(final List<?> aListWithSomeType) {
	        // Here I'd like to get the Class-Object 'SpiderMan'
	        System.out.println(aListWithSomeType.getClass().getGenericSuperclass());
	        return ((ParameterizedType) aListWithSomeType
	            .getClass()
	            .getGenericSuperclass()).getActualTypeArguments()[0] + "";
	    }
	    public static void main(String... args) throws Throwable {
	        chill(new ArrayList<SpiderMan>() {});
	        
	        System.out.println(chill(new ArrayList<SpiderMan>() {}));
	    }
	}
	class SpiderMan {
	}
	
	@ToString
	public static class TestClass {
		
		public String helloWorldStr = "Hello, World!";
		public int helloInt = 4;
		
		@CommentSwconf("Hello, World!")
		public TestClass2 testClass2 = new TestClass2();
		
	}
	
}
