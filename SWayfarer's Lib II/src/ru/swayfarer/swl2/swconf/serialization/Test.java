package ru.swayfarer.swl2.swconf.serialization;

import ru.swayfarer.swl2.swconf.primitives.SwconfObject;

public class Test {

	public static void main(String[] args)
	{
		String swconf = "r = 4, d = '434343', name = {qwe = '323323', e = 5}, arr = [1, 2, 3, 4, 5, 6]";
		
		SwconfReader reader = new SwconfReader();
		SwconfWriter writer = new SwconfWriter();
		
		SwconfObject object = reader.readSwconf(swconf);
		
		writer.write(object);
		
		System.out.println(writer.toSwconfString());
	}
	
}
