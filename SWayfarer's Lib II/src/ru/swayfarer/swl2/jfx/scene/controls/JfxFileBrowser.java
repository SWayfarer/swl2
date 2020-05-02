package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.jfx.scene.controls.JfxFileChooser.JfxFileChooserWindow;
import ru.swayfarer.swl2.jfx.scene.controls.JfxFileChooser.JfxFileChoosingOptions;
import ru.swayfarer.swl2.jfx.scene.controls.ui.UiFileBrowser;
import ru.swayfarer.swl2.jfx.scene.layout.JfxAnchorPane;
import ru.swayfarer.swl2.jfx.tags.window.JfxWindow;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.observable.property.ObservableProperty.PropertyChangeEvent;
import ru.swayfarer.swl2.resource.file.FileSWL;

@SuppressWarnings("unchecked")
public class JfxFileBrowser extends JfxAnchorPane {

	public boolean isUsingLocalPath = false;
	public boolean isUsingRlinks = false;
	
	public UiFileBrowser ui = new UiFileBrowser();
	
	public JfxFileChoosingOptions fileChoosingOptions = new JfxFileChoosingOptions();
	public ObservableProperty<JfxFileFilter> fileFiler = Observables.createProperty();
	
	public JfxFileBrowser()
	{
		fileFiler.setPost(true);
		
		ui.initParent(this);
		
		ui.btnBrowseFile.eventsMouse.clicked.subscribe((event) -> {
			
			if (event.getButton() == MouseButton.PRIMARY)
			{
				JfxFileChooser fileChooser = new JfxFileChooser();
				
				fileChooser.fileChoosingOptions = fileChoosingOptions;
				fileChooser.setFilters(fileFiler.get());
				JfxFileChooserWindow window = fileChooser.toFileChooserWindow("Select a file...");
				
				FileSWL file = window.getResult();
				
				if (file != null)
				{
					String path = isUsingLocalPath ? file.getLocalPath() : file.getAbsolutePath();
					
					if (isUsingRlinks)
						path = "f:" + path;
					
					ui.txtFilePath.setText(path);
				}
			}
		});
	}
	
	public void validateFilePath()
	{
		ui.txtFilePath.textProperty().addListener((item, oldValue, newValue) -> {
			
			JfxFileFilter fileFilter = this.fileFiler.get();
			
			if (fileFilter != null && !fileFilter.filterFun.apply(new FileSWL(newValue)))
			{
				ui.txtFilePath.setBackgroundColor(Color.RED);
			}
			else
			{
				ui.txtFilePath.setTextColor(Color.WHITE);
			}
		});
	}
}
