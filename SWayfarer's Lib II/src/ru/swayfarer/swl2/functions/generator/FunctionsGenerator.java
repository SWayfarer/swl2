package ru.swayfarer.swl2.functions.generator;

import ru.swayfarer.swl2.functions.GeneratedFunctions;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.string.templategenerator.TemplatedStringGenerator;

/**
 * Генератор функций класса {@link GeneratedFunctions} 
 * <br> Использует файлы (templates/header/template/ending).txt из подпакета для генерации функций, подставляя в них разные аргументы
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class FunctionsGenerator {
	
	/** Файл, куда все будет сохраняться (т.н. "Выходной файл") */
	@InternalElement
	public FileSWL outFile = new FileSWL("output.txt").createIfNotFoundSafe();
	
	/** Базовое имя функции*/
	@InternalElement
	public String functionName = "IFunction";
	
	/** Generic возвращаемого типа */
	@InternalElement
	public String returnType = "Ret_Type";
	
	/** Максимальное кол-во аргументов */
	@InternalElement
	public int maxArgsCount = 32;
	
	/** Сгенерировать функции */
	public void generate()
	{
		FileSWL file = outFile;
		
		System.out.println("Created file at " + file.getAbsolutePath());
		
		DataOutputStreamSWL dos = file.toOutputStream();
		
		String template = RLUtils.pkg("templates/template.txt").toSingleString();
		TemplatedStringGenerator stringGenerator = new TemplatedStringGenerator(template);
		stringGenerator.reader.setLineSplitter(StringUtils.CRLF);
		
		DynamicString declareArgs = new DynamicString();
		DynamicString argsGenerics = new DynamicString();
		DynamicString invokeArgs = new DynamicString();
		DynamicString invokeArgs2 = new DynamicString();
		
		dos.writeString(RLUtils.pkg("templates/header.txt").toSingleString());
		
		for (int i1 = 0; i1 <= maxArgsCount; i1 ++)
		{
			stringGenerator.clear();
			
			if (i1 > 0)
			{
				if (!argsGenerics.isEmpty())
					argsGenerics.append(", ");
				
				if (!invokeArgs.isEmpty())
					invokeArgs.append(", ");
				
				if (!invokeArgs2.isEmpty())
					invokeArgs2.append(", ");
				
				if (!declareArgs.isEmpty())
					declareArgs.append(", ");
				
				argsGenerics.append("Arg", i1);
				invokeArgs.append("arg", i1);
				invokeArgs2.append("a", i1);
				declareArgs.append("Arg", i1, " arg", i1);
			}
			
			stringGenerator.setProperty("funGenerics", argsGenerics);
			stringGenerator.setProperty("invokeArgs", invokeArgs);
			stringGenerator.setProperty("invokeArgs2", invokeArgs2);
			stringGenerator.setProperty("returnType", returnType);
			stringGenerator.setProperty("functionName", functionName);
			stringGenerator.setProperty("argsCount", i1);
			stringGenerator.setProperty("declareArgs", declareArgs);
			
			String str = stringGenerator.generate();
			dos.writeString(str);
		}
		
		dos.writeString(RLUtils.pkg("templates/ending.txt").toSingleString());
		
		dos.closeSafe();
	}
	
	/** Задать {@link #outFile} */
	public <T extends FunctionsGenerator> T setOutFile(FileSWL file) 
	{
		this.outFile = file;
		return (T) this;
	}
	
	/** Задать {@link #functionName} */
	public <T extends FunctionsGenerator> T setFunctionName(@ConcattedString Object... name) 
	{
		this.functionName = StringUtils.concat(name);
		return (T) this;
	}
	
	/** Задать {@link #maxArgsCount} */
	public <T extends FunctionsGenerator> T setMaxArgs(int argsCount) 
	{
		this.maxArgsCount = argsCount;
		return (T) this;
	}
	
	/**
	 * Чтобы можно было запустить как отдельный софт 
	 */
	public static void main(String[] args)
	{
		new FunctionsGenerator().generate();
	}
	
	/**
	 * Сгенерировать файл с функциями 
	 * @param file Файл, куда все будет сохраняться (т.н. "Выходной файл") 
	 * @param maxArgs Максимальное кол-во аргументов 
	 */
	public static void generate(FileSWL file, int maxArgs)
	{
		new FunctionsGenerator().setMaxArgs(maxArgs).setOutFile(file).generate();
	}
	
	/**
	 * Сгенерировать файл с функциями в %appDir%/output.txt
	 * @param maxArgs Максимальное кол-во аргументов 
	 */
	public static void generate(int maxArgs)
	{
		new FunctionsGenerator().setMaxArgs(maxArgs).generate();
	}
	
}
