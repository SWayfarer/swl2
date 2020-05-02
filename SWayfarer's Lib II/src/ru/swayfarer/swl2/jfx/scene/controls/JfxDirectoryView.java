package ru.swayfarer.swl2.jfx.scene.controls;
import java.util.Comparator;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.jfx.scene.layout.JfxAnchorPane;
import ru.swayfarer.swl2.jfx.scene.layout.JfxHBox;
import ru.swayfarer.swl2.jfx.scene.layout.JfxStackedPane;
import ru.swayfarer.swl2.jfx.utils.JfxIconManager;
import ru.swayfarer.swl2.jfx.utils.JfxUtils;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.observable.property.ObservableProperty.PropertyChangeEvent;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.resource.file.FileSWL;

public class JfxDirectoryView extends JfxStackedPane {
	
	public static Image folderImage = JfxIconManager.instance.getIcon(25, 25, "/assets/swl2/icons/folder.png");
	public static Image	fileImage = JfxIconManager.instance.getIcon(25, 25, "/assets/swl2/icons/file.png");
	
	public static final Comparator<FileSWL> explorerFilesComparator = (f1, f2) -> {
		if (f1.isDirectory() && f2.isFile())
			return -1;
		else if (f1.isFile() && f2.isDirectory())
			return 1;
		else 
			return f1.getName().compareTo(f2.getName());
	};
	
	public IObservable<JfxDirectoryView.Item> eventItemCreation = Observables.createObservable();
	public ObservableProperty<JfxDirectoryView.Item> selectedItem = Observables.createProperty();
	public ObservableProperty<FileSWL> currentDir = Observables.createProperty();
	
	public IExtendedList<ISubscription<MouseEvent>> clickSubs = CollectionsSWL.createExtendedList();
	
	public ObservableProperty<IFunction1<FileSWL, Boolean>> currentFilter = Observables.createProperty();
	
	public JfxDirectoryView()
	{
		currentDir.subscribe(this::onCurrentDirectoryChanged);
		currentFilter.subscribe(this::onFilterChanged);
		
		currentDir.setValue(new FileSWL("."));
		
		selectedItem.subscribe(this::onSelectedItemChanged);
		
		eventsKeyboard.keyPressed.subscribe((event) -> {
			
			System.out.println(event.getCode());
			
			if (event.getCode() == KeyCode.BACK_SPACE)
			{
				back();
			}
		});
	}
	
	public void back()
	{
		FileSWL parent = currentDir.get().getParentFile();
		
		if (parent != null)
		{
			currentDir.setValue(parent);
		}
	}
	
	public void onSelectedItemChanged(PropertyChangeEvent event)
	{
		JfxDirectoryView.Item oldItem = event.getOldValue();
		JfxDirectoryView.Item newItem = event.getNewValue();
		
		if (oldItem != null)
		{
			oldItem.selectionRectangle.setOpacity(0);
		}
		
		if (newItem != null)
		{
			newItem.selectionRectangle.setOpacity(1);
		}
	}
	
	public void updateContent(FileSWL newRoot, IFunction1<FileSWL, Boolean> filter)
	{
		this.clear();
		clickSubs.each(ISubscription::dispose);
		clickSubs.clear();
		
		IExtendedList<FileSWL> subfiles = newRoot.getSubfiles();
		
		subfiles.sort(explorerFilesComparator);
		
		for (FileSWL file : subfiles)
		{
			if (file.isDirectory() || filter == null || filter.apply(file))
			{
				JfxDirectoryView.Item listItem = new Item(file);
				eventItemCreation.next(listItem);
				
				clickSubs.add(listItem.eventsMouse.clicked.subscribe((event) -> {
					if (file.isDirectory() && event.getClickCount() > 1)
						currentDir.setValue(file);
					else 
						selectedItem.setValue(listItem);
				}));
				
				clickSubs.add(listItem.eventsMouse.entered.subscribe((event) -> {
					if (selectedItem.get() != listItem)
						listItem.selectionRectangle.setOpacity(0.5);
				}));
				
				clickSubs.add(listItem.eventsMouse.exited.subscribe((event) -> {
					if (selectedItem.get() != listItem)
						listItem.selectionRectangle.setOpacity(0);
				}));
				
				this.add(listItem);
			}
		}
	}
	
	public void onFilterChanged(PropertyChangeEvent changeEvent)
	{
		updateContent(currentDir.get(), changeEvent.getNewValue());
	}
	
	public void onCurrentDirectoryChanged(PropertyChangeEvent changeEvent)
	{
		updateContent(changeEvent.getNewValue(), currentFilter.get());
	}
	
	public static class Item extends JfxAnchorPane {
		
		public JfxText text;
		public JfxImageView imageView;
		public FileSWL file;
		public Rectangle selectionRectangle;
		
		public Item(FileSWL file)
		{
			JfxHBox hBox = new JfxHBox();
			
			if (file.isFile())
				imageView = new JfxImageView(fileImage);
			else
				imageView = new JfxImageView(folderImage);
			
			text = new JfxText(file.getName());
			selectionRectangle = new Rectangle(getWidth(), getHeight(), Color.CADETBLUE);
			
			JfxUtils.fitTo(this, selectionRectangle);
			
			hBox.setAlignment(Pos.CENTER_LEFT);
			hBox.setSpacing(4);
			
			hBox.addItems(imageView, text);
			selectionRectangle.setOpacity(0);
			text.setFill(Color.GRAY);
			addItems(selectionRectangle, hBox);
			
			this.file = file;
		}
		
	}
}