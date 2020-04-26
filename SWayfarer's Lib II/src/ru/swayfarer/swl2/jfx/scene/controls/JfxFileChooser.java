package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.collections.FXCollections;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.observable.IObservableList;
import ru.swayfarer.swl2.jfx.scene.controls.JfxButton;
import ru.swayfarer.swl2.jfx.scene.controls.JfxComboBox;
import ru.swayfarer.swl2.jfx.scene.controls.JfxText;
import ru.swayfarer.swl2.jfx.scene.controls.JfxTextField;
import ru.swayfarer.swl2.jfx.scene.layout.JfxHBox;
import ru.swayfarer.swl2.jfx.scene.layout.JfxSplitPane;
import ru.swayfarer.swl2.jfx.scene.layout.JfxStackedPane;
import ru.swayfarer.swl2.jfx.scene.layout.JfxVBox;
import ru.swayfarer.swl2.jfx.utils.JfxIconManager;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.observable.property.ObservableProperty.ChangeEvent;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.system.SystemUtils;
import ru.swayfarer.swl2.tasks.RecursiveSafeTask;

public class JfxFileChooser extends JfxVBox {
	
	public boolean isAcceptsFiles = true;
	public boolean isAcceptsDirs;
	public boolean isAcceptsNonExisting;
	
	public IObservableList<String> directoriesHistory = CollectionsSWL.createObservableList();
	
	public JfxDirectoryView filesView = new JfxDirectoryView();
	
	public JfxTextField txtSelectedFile = new JfxTextField();
	public JfxTextField txtCurrentDir = new JfxTextField();
	
	public JfxComboBox<JfxFileFilter> cmbAvailableFilters = new JfxComboBox<>();
	
	public JfxButton btnOk = new JfxButton("Ok");
	public JfxButton btnCancel = new JfxButton("Cancel");
	
	public JfxButton btnNewFolder = new JfxButton();
	public JfxButton btnBack = new JfxButton();
	public JfxButton btnToParent = new JfxButton();
	
	public JfxHBox buttonsBox = new JfxHBox(txtSelectedFile, cmbAvailableFilters, btnOk, btnCancel);
	public JfxHBox topButtonsBox = new JfxHBox(btnBack, btnToParent, txtCurrentDir, btnNewFolder);
	
	public JfxFileChooser.SidePane sidePane = new SidePane();
	
	public JfxSplitPane pnFiles = new JfxSplitPane(sidePane, filesView);
	
	public ObservableProperty<FileSWL> result = Observables.createProperty();
	
	public RecursiveSafeTask historyExecuter = new RecursiveSafeTask();
	
	public static Image backButtonIcon = JfxIconManager.instance.getIcon(15, 15, "/assets/swl2/icons/undo.png");
	public static Image newFolderIcon = JfxIconManager.instance.getIcon(15, 15, "/assets/swl2/icons/folder.png");
	public static Image toParentButtonIcon = JfxIconManager.instance.getIcon(15, 15, "/assets/swl2/icons/left_up2.png");
	
	public JfxFileChooser()
	{
		result.subscribe((e) -> System.out.println(e.newValue));
		
		addItems(topButtonsBox, pnFiles, buttonsBox);
		
		pnFiles.setDividerPosition(0.25f, true);
		
		sidePane.eventFileOpen.subscribe((e) -> {
			filesView.currentDir.setValue(e);
		});
		
		setSpacing(6);
		
		topButtonsBox.setMarigins(5, 5, 5, 0);
		topButtonsBox.setSpacing(5);
		
		buttonsBox.setMarigins(5, 0, 5, 5);
		buttonsBox.setSpacing(5);
		
		btnBack.setIcon(backButtonIcon);
		btnNewFolder.setIcon(newFolderIcon);
		btnToParent.setIcon(toParentButtonIcon);
		
		cmbAvailableFilters.selectedItem.subscribe((event) -> {
			onFilterSelected(event.getNewValue());
		});
		
		btnOk.eventsMouse.clicked.subscribe(this::onOkButtonClicked);
		btnCancel.eventsMouse.clicked.subscribe(this::onCancelButtonClicked);
		
		btnBack.eventsMouse.clicked.subscribe(this::onBackButtonClicked);
		btnToParent.eventsMouse.clicked.subscribe(this::onToParentButtonClicked);
		
		txtCurrentDir.setHorizontalSizePolicy(Priority.ALWAYS);
		txtSelectedFile.setHorizontalSizePolicy(Priority.ALWAYS);
		txtSelectedFile.eventsKeyboard.keyPressed.subscribe(this::onCurrentFileKeyPressed);
		txtCurrentDir.eventsKeyboard.keyPressed.subscribe(this::onCurrentDirKeyPressed);
		
		filesView.fitToParent();
		
		filesView.eventItemCreation.subscribe(this::onItemCreated);
		filesView.selectedItem.subscribe(this::onSelectedItemChanged);
		filesView.currentDir.subscribe(this::onCurrentDirChanged);
		
		pnFiles.setVerticalSizePolicy(Priority.ALWAYS);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends JfxFileChooser> T addExtensionsFilter(String name, String desc, String... exts) 
	{
		addFilters(new JfxFileFilter.ExtensionsFilter(name, desc, exts));
		return (T) this;
	}
	
	public void onCurrentDirKeyPressed(KeyEvent keyEvent)
	{
		if (keyEvent.getCode() == KeyCode.ENTER)
		{
			FileSWL file = new FileSWL(txtCurrentDir.getText());
			
			if (file.isExistingDir())
			{
				filesView.currentDir.setValue(file);
			}
		}
	}
	
	public void onCurrentFileKeyPressed(KeyEvent keyEvent)
	{
		if (keyEvent.getCode() == KeyCode.ENTER)
		{
			acceptSelected();
		}
	}
	
	public void onToParentButtonClicked(MouseEvent mouseEvent)
	{
		if (mouseEvent.getButton() == MouseButton.PRIMARY)
		{
			filesView.back();
		}
	}
	
	public void onFilterSelected(JfxFileFilter filter)
	{
		filesView.currentFilter.setValue(filter == null ? null : filter.filterFun);
	}
	
	public void onBackButtonClicked(MouseEvent mouseEvent)
	{
		if (mouseEvent.getButton() == MouseButton.PRIMARY)
		{
			historyExecuter.start(() -> {
				
				directoriesHistory.removeLastElement();
				
				String path = directoriesHistory.getLastElement();
				
				System.out.println(directoriesHistory);
				System.out.println("Path: " + path);
				
				if (!StringUtils.isEmpty(path))
				{
					FileSWL historyDir = new FileSWL(path);
					
					if (historyDir.isExistingDir())
						filesView.currentDir.setValue(historyDir);
				}
			});
		}
	}
	
	public void onCurrentDirChanged(ChangeEvent changeEvent)
	{
		FileSWL newDir = changeEvent.getNewValue();
		
		historyExecuter.start(() -> {
			directoriesHistory.add(newDir.getAbsolutePath());
		});
		
		this.txtCurrentDir.setText(newDir.getAbsolutePath());
	}
	
	public void onSelectedItemChanged(ChangeEvent changeEvent)
	{
		JfxDirectoryView.Item newItem = changeEvent.getNewValue();
		
		if (newItem != null && newItem.file != null)
		{
			txtSelectedFile.setText(newItem.file.getName());
		}
	}
	
	public void onOkButtonClicked()
	{
		acceptSelected();
	}
	
	public void onCancelButtonClicked(MouseEvent event)
	{
		result.setValue(null);
	}
	
	public void onItemCreated(JfxDirectoryView.Item item)
	{
		item.eventsMouse.clicked.subscribe((event) -> {
			
			if (event.getClickCount() > 1)
			{
				FileSWL file = item.file;
				
				if (file != null)
				{
					if (file.isDirectory())
					{
						filesView.currentDir.setValue(file);
					}
					else
					{
						acceptSelected();
					}
				}
			}
		});
	}
	
	public void acceptSelected()
	{
		if (isFilterAcceptsSelected())
		{
			result.setValue(getSelectedFile());
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends JfxFileChooser> T addFilters(JfxFileFilter... filters) 
	{
		cmbAvailableFilters.getItems().addAll(filters);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends JfxFileChooser> T setFilters(JfxFileFilter... filters) 
	{
		cmbAvailableFilters.getItems().setAll(FXCollections.observableArrayList(filters));
		cmbAvailableFilters.getSelectionModel().selectFirst();
		
		return (T) this;
	}
	
	public void updateContent()
	{
		
	}
	
	public boolean isFilterAcceptsSelected()
	{
		FileSWL currentFile = getSelectedFile();
		
		if (isFilterAccepts(currentFile))
			return true;
		
		return isFilterAccepts(new FileSWL(txtSelectedFile.getText()));
	}
	
	public boolean isFilterAccepts(FileSWL file)
	{
		JfxFileFilter filter = getCurrentFileFilter();
		
		if (file == null)
			return false;
		
		if (file.isDirectory() && !isAcceptsDirs)
			return false;
		
		if (file.isFile() && !isAcceptsFiles)
			return false;
		
		if (!file.exists() && !isAcceptsNonExisting)
			return false;
		
		
		if (filter != null && filter.filterFun != null)
		{
			return filter.filterFun.apply(file);
		}
		
		return true;
	}

	public FileSWL getSelectedFile()
	{
		FileSWL currentDir = filesView.currentDir.get();
		
		if (currentDir != null)
		{
			FileSWL subFile = currentDir.subFile(txtSelectedFile.getText());
			
			return subFile;
		}
		
		return null;
	}
	
	public JfxFileFilter getCurrentFileFilter()
	{
		return cmbAvailableFilters.getValue();
	}
	
	public static class SidePane extends JfxStackedPane {
		
		public static Image deviceImage = JfxIconManager.instance.getIcon(25, 25, "/assets/swl2/icons/device.png");
		
		public static int elementSpacing = 5;
		
		public JfxVBox bookmarks = new JfxVBox(elementSpacing);
		public JfxVBox devices = new JfxVBox(elementSpacing);
		
		public JfxText txtBookmarks = new JfxText("Bookmarks");
		public JfxText txtDevices = new JfxText("Devices");
		
		public IObservable<FileSWL> eventFileOpen = Observables.createObservable();
		
		public SidePane()
		{
			setHeaderFont(txtDevices);
			setHeaderFont(txtBookmarks);
			
			setCategoryMargin(bookmarks);
			setCategoryMargin(devices);
			
			setSpacing(10);
			setAlligment(AlligmentType.VERTICAL);
			
			loadDefaultDevices();
			loadDefaultBookmarks();

			addItems(txtBookmarks, bookmarks, txtDevices, devices);
		}
		
		public void configureItem(JfxDirectoryView.Item item)
		{
			if (item != null)
			{
				item.eventsMouse.clicked.subscribe((event) -> {
					
					if (event.getClickCount() > 1)
					{
						eventFileOpen.next(item.file);
					}
				});
				
				item.eventsMouse.entered.subscribe((event) -> {
					item.selectionRectangle.setOpacity(0.5);
				});
				
				item.eventsMouse.exited.subscribe((event) -> {
					item.selectionRectangle.setOpacity(0);
				});
			}
		}
		
		public void setCategoryMargin(JfxVBox vBox)
		{
			vBox.setMarigins(15, 0, 0, 0);
		}
		
		public void setHeaderFont(JfxText text)
		{
			text.setFont(Font.font(text.getFont().getFamily(), FontWeight.BOLD, 15));
		}
		
		public void loadDefaultDevices()
		{
			IExtendedList<FileSWL> mountedDevices = SystemUtils.getMountedDevices().getSubfiles();
			mountedDevices.each(this::addDeviceItem);
		}
		
		public void loadDefaultBookmarks()
		{
			addBookmarkItem(new FileSWL("%user%"), "Home");
			addBookmarkItem(new FileSWL("%appDir%"), null);
		}
		
		public void addBookmarkItem(FileSWL file, String name)
		{
			JfxDirectoryView.Item item = new JfxDirectoryView.Item(file);
			configureItem(item);
			
			if (!StringUtils.isEmpty(name))
				item.text.setText(name);
			
			bookmarks.addItem(item);
		}
		
		public void addDeviceItem(FileSWL device)
		{
			JfxDirectoryView.Item item = new JfxDirectoryView.Item(device);
			configureItem(item);
			
			if (item.text.getText().isEmpty())
				item.text.setText("/");
			
			if (deviceImage != null)
			{
				item.imageView.setImage(deviceImage);
			}
			
			devices.addItem(item);
		}
		
	}
}