package ru.swayfarer.swl2.swconf.serialization;

import lombok.ToString;
import ru.swayfarer.swl2.swconf.primitives.SwconfObject;
import ru.swayfarer.swl2.swconf.serialization.formatter.PrettySwconfFormatter;

public class Test {

	public static void main(String[] args)
	{
		String swconf = "r = 4, d = '434343', name = {qwe = '323323', e = 5}, arr = [1, 2, 3, 4, 5, 6]";
		
		SwconfReader reader = new SwconfReader();
		SwconfWriter writer = new SwconfWriter();
		SwconfSerialization serialization = new SwconfSerialization();
		
		TestClass testClass = new TestClass();
		testClass.testClass2.someBoolean = true;
		
		SwconfObject object = serialization.serialize(testClass);
		
		TestClass class1 = serialization.deserialize(TestClass.class, object);
		
		System.out.println("Getted obj " + class1);
		
		writer.registeredFormatters.add(new PrettySwconfFormatter());
		writer.write(object);
		
		System.out.println(writer.toSwconfString());
	}
	
	@ToString
	public static class TestClass2 {
		
		public boolean someBoolean = false;
		
	}
	
	@ToString
	public static class TestClass {
		
		public String helloWorldStr = "Hello, World!";
		public int helloInt = 4;
		
		public TestClass2 testClass2 = new TestClass2();
		
	}
	
}
