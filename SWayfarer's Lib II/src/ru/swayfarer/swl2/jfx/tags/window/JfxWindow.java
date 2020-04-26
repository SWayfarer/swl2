package ru.swayfarer.swl2.jfx.tags.window;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.swayfarer.swl2.jfx.css.CssManager;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper.ResizeEvent;
import ru.swayfarer.swl2.jfx.scene.layout.JfxVBox;
import ru.swayfarer.swl2.jfx.utils.FXResizeHelper;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.observable.property.ObservableProperty.ChangeEvent;
import ru.swayfarer.swl2.tasks.RecursiveSafeTask;

@SuppressWarnings("unchecked")
public class JfxWindow {

	public double lastWidth, lastHeight;
	
	public IObservable<JfxWindowShowingEvent> eventShowingChanged = Observables.createObservable();
	public IObservable<ResizeEvent> eventResize = Observables.createObservable();
	
	public ScaleEventsHelper eventsResize = new ScaleEventsHelper();
	
	public RecursiveSafeTask recSafeExecutor = new RecursiveSafeTask();
	
	public ObservableProperty<Stage> stage = Observables.createProperty();
	public ObservableProperty<Scene> scene = Observables.createProperty();
	public ObservableProperty<Parent> rootElement = Observables.createProperty();

	public FXResizeHelper fxResizeHelper;
	
	public JfxWindow(Parent parent)
	{
		stage.setPost(true);
		scene.setPost(true);
		rootElement.setPost(true);
		
		rootElement.subscribe(this::onRootElementChanged);
		stage.subscribe(this::onStageChanged);
		scene.subscribe(this::onSceneChanged);
		
		this.rootElement.setValue(parent);
	}
	
	public void onSceneChanged(ChangeEvent event)
	{
		if (stage.isNull())
			stage.setValue(new Stage());
		
		Scene newScene = event.getNewValue();
		newScene.setRoot(rootElement.get());
		CssManager.instance.addScene(newScene);
		stage.get().setScene(newScene);
	}
	
	public void onStageChanged(ChangeEvent event)
	{
		Stage newStage = event.getNewValue();
		newStage.setScene(scene.get());
	}
	
	public <T extends JfxTitleDecorator> T setCustomDecorator(@ConcattedString Object... text) 
	{
		setDecorated(false);
		
		JfxVBox vBox = new JfxVBox();
		
		JfxTitleDecorator titleDecorator = new JfxTitleDecorator(this, text);
		vBox.addItems(titleDecorator, rootElement.get());
		
		rootElement.setValue(vBox);
		
		vBox.eventsScale.resize.subscribe((event) -> {
			titleDecorator.setWidth(event.width);
		});
		
		fxResizeHelper = new FXResizeHelper(stage.get(), 10, 10);
		
		return (T) titleDecorator;
	}
	
	public void onRootElementChanged(ChangeEvent event)
	{
		Parent newNode = event.getNewValue();
		
		System.out.println("Changing root to" + newNode);
		
		
		if (scene.isNull())
			scene.setValue(new Scene(newNode));
		else
			scene.get().setRoot(newNode);
	}
	
	public <T extends JfxWindow> T setRootElement(Parent node) 
	{
		rootElement.setValue(node);
		return (T) this;
	}
	
	public <T extends JfxWindow> T setStage(Stage stage) 
	{
		configureStage(stage);
		this.stage.setValue(stage);
		
		return (T) this;
	}
	
	public boolean isMaximized()
	{
		if (stage.isNull())
			return false;
		
		return stage.get().isMaximized();
	}
	
	public <T extends JfxWindow> T setMaximized(boolean isMaximized) 
	{
		if (stage.isNull())
			return (T) this;
		
		stage.get().setMaximized(isMaximized);
		
		return (T) this;
	}
	
	public void configureStage(Stage stage)
	{
		if (stage == null)
			return;
		
		stage.showingProperty().addListener((item, oldValue, newValue) -> {
			eventShowingChanged.next(JfxWindowShowingEvent.of(newValue, this));
		});
		
		eventsResize.init(stage);
	}
	
	public boolean isIconified()
	{
		return stage.isNull() ? false : stage.get().isIconified();
	}
	
	public <T extends JfxWindow> T setIconified(boolean isIconified) 
	{
		if (stage.isNull())
			return (T) this;
		
		stage.get().setIconified(isIconified);
		
		return (T) this;
	}
	
	public <T extends JfxWindow> T setScene(Scene scene) 
	{
		this.scene.setValue(scene);
		return (T) this;
	}
	
	public <T extends JfxWindow> T showAndWait() 
	{
		if (stage.isNull())
			return (T) this;
		
		stage.get().showAndWait();
		
		return (T) this;
	}
	
	public <T extends JfxWindow> T show() 
	{
		if (stage.isNull())
			return (T) this;
		
		stage.get().show();
		
		return (T) this;
	}
	
	public <T extends JfxWindow> T close() 
	{
		if (stage.isNull())
			return (T) this;
		
		this.stage.get().close();
		
		return (T) this;
	}
	
	public <T extends JfxWindow> T disableModality() 
	{
		if (this.stage.isNull())
			return (T) this;
		
		stage.get().initModality(Modality.NONE);
		
		return (T) this;
	}
	
	public <T extends JfxWindow> T setModality(Stage parent) 
	{
		if (this.stage.isNull())
			return (T) this;
		
		Stage stage = this.stage.get();
		
		if (parent != null)
		{
			stage.initOwner(parent);
			stage.initModality(Modality.WINDOW_MODAL);
		}
		else
		{
			stage.initModality(Modality.APPLICATION_MODAL);
		}
		
		return (T) this;
	}
	
	public <T extends JfxWindow> T setDecorated(boolean isDecorated) 
	{
		if (stage.isNull())
			return (T) this;
		
		stage.get().initStyle(StageStyle.UNDECORATED);
		
		return (T) this;
	}
	
	public <T extends JfxWindow> T setSize(double width, double height) 
	{
		if (stage.isNull())
			return (T) this;
		
		Stage stage = this.stage.get();
		
		if (width > 0)
			stage.setWidth(width);
		
		if (height > 0)
			stage.setHeight(height);
		
		return (T) this;
	}
	
	public <T extends JfxWindow> T setMinSize(double width, double height) 
	{
		if (stage.isNull())
			return (T) this;
		
		Stage stage = this.stage.get();
		
		if (width > 0)
			stage.setMinWidth(width);
		
		if (height > 0)
			stage.setMinHeight(height);
		
		return (T) this;
	}
	
	public <T extends JfxWindow> T setMaxSize(double width, double height) 
	{
		if (stage.isNull())
			return (T) this;
		
		Stage stage = this.stage.get();
		
		if (width > 0)
			stage.setMaxWidth(width);
		
		if (height > 0)
			stage.setMaxHeight(height);
		
		return (T) this;
	}
}
