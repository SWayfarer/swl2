package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.jfx.scene.controls.ui.UiFileBrowser;
import ru.swayfarer.swl2.jfx.scene.layout.JfxAnchorPane;

@SuppressWarnings("unchecked")
public class JfxFileBrowser extends JfxAnchorPane {

	public UiFileBrowser ui = new UiFileBrowser();
	
	public IExtendedList<IFunction1<String, Boolean>> filters = CollectionsSWL.createExtendedList();
	
	public JfxFileBrowser()
	{
		ui.initParent(this);
		
		ui.txtFilePath.textProperty().addListener((item, oldValue, newValue) -> {
			
			if (filters.contains((e) -> !e.apply(newValue)))
			{
				ui.txtFilePath.setTextColor(Color.RED);
			}
			else
			{
				ui.txtFilePath.setTextColor(Color.WHITE);
			}
		});
		
		ui.btnBrowseFile.eventsMouse.clicked.subscribe((event) -> {
			
			if (event.getButton() == MouseButton.PRIMARY)
			{
				
			}
		});
	}
	
	public <T extends JfxFileBrowser> T addFilter(IFunction1<String, Boolean> filter) 
	{
		filters.addExclusive(filter);
		return (T) this;
	}
	
}
