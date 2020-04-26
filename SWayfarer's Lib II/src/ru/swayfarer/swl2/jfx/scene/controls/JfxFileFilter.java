package ru.swayfarer.swl2.jfx.scene.controls;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.resource.file.FileSWL;

public class JfxFileFilter {
	
	public String name, description;
	public IFunction1<FileSWL, Boolean> filterFun;
	
	public JfxFileFilter(String name, String description, IFunction1<FileSWL, Boolean> filterFun)
	{
		super();
		this.name = name;
		this.description = description;
		this.filterFun = filterFun;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	public static class ExtensionsFilter extends JfxFileFilter {

		public IExtendedList<String> extensions = CollectionsSWL.createExtendedList();
		
		public ExtensionsFilter(String name, String description, String... extsWithotPoint)
		{
			super(name, description, null);
			
			filterFun = (f) -> {
				
				String fname = f.getName();
				
				for (String ext : extensions)
				{
					if (fname.endsWith("." + ext))
						return true;
				}
				
				return false;
			};
			
			extensions.addAll(extsWithotPoint);
		}
	}
}