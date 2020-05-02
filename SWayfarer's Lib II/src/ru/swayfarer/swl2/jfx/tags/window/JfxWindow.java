package ru.swayfarer.swl2.jfx.tags.window;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.swayfarer.swl2.jfx.css.CssManager;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper;
import ru.swayfarer.swl2.jfx.helpers.ScaleEventsHelper.ResizeEvent;
import ru.swayfarer.swl2.jfx.scene.controls.IJavafxWidget;
import ru.swayfarer.swl2.jfx.scene.layout.JfxVBox;
import ru.swayfarer.swl2.jfx.utils.FXResizeHelper;
import ru.swayfarer.swl2.jfx.utils.JfxUtils;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.observable.property.ObservableProperty.PropertyChangeEvent;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;
import ru.swayfarer.swl2.string.StringUtils;
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
	
	public ObservableProperty<Image> windowIcon = Observables.createProperty();

	public FXResizeHelper fxResizeHelper;
	
	public ILogger logger = LoggingManager.getLogger(getClass().getSimpleName());
	
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
	
	public <T extends JfxWindow> T setSizeFromRoot() 
	{
		Parent root = rootElement.get();
		Stage stage = this.stage.get();
		
		if (root instanceof Region)
		{
			Region parentRg = (Region) root;
			stage.setWidth(parentRg.getPrefWidth());
			stage.setHeight(parentRg.getPrefHeight());
		}
		
		return (T) this;
	}
	
	public <T extends JfxWindow> T setIcon(@ConcattedString Object... rlinkPath) 
	{
		return setIcon(RLUtils.createLink(StringUtils.concat(rlinkPath)));
	}
	
	public <T extends JfxWindow> T setIcon(ResourceLink rlink) 
	{
		return logger.safeReturn(() -> {
			Image image = new Image(rlink.toStream());
			return setIcon(image);
		}, (T) this, "Error while setting icon for", this, "from rlink", rlink);
	}
	
	public <T extends JfxWindow> T setIcon(Image image) 
	{
		Stage stage = this.stage.get();
		
		ObservableList<Image> stageIcons = stage.getIcons();
		
		if (image != null)
			stageIcons.setAll(image);
		else
			stageIcons.clear();
		
		return (T) this;
	}
	
	public <T extends JfxWindow> T setTitle(@ConcattedString Object... text) 
	{
		this.stage.get().setTitle(StringUtils.concat(text));
		return (T) this;
	}
	
	public void onSceneChanged(PropertyChangeEvent event)
	{
		if (stage.isNull())
			stage.setValue(new Stage());
		
		Scene newScene = event.getNewValue();
		newScene.setRoot(rootElement.get());
		CssManager.instance.addScene(newScene);
		CssManager.instance.apply(newScene);
		stage.get().setScene(newScene);
	}
	
	public void onStageChanged(PropertyChangeEvent event)
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
		
		vBox.eventsScale.resize.subscribe((event) -> {
			titleDecorator.setWidth(event.width);
		});
		
		if (rootElement.get() instanceof Region)
		{
			Region rg = (Region) rootElement.get();
			rg.setMaxHeight(Double.POSITIVE_INFINITY);
			rg.setMaxWidth(Double.POSITIVE_INFINITY);
		}
		
		if (rootElement.get() instanceof IJavafxWidget)
		{
			((IJavafxWidget)rootElement.get()).setVerticalSizePolicy(Priority.ALWAYS);
		}
		
		rootElement.setValue(vBox);
		
		ScaleEventsHelper helper = new ScaleEventsHelper().init(stage.get());
		
		helper.resize.subscribe((event) -> {
			vBox.setMaxWidth(event.width);
			vBox.setMaxHeight(event.height);
		});
		
		fxResizeHelper = new FXResizeHelper(stage.get(), 10, 5);
		
		return (T) titleDecorator;
	}
	
	public <T extends JfxWindow> T centered() 
	{
		Platform.runLater(() -> {
			Rectangle2D screen = JfxUtils.getScreenResolution();
			Stage stage = this.stage.get();
			
			stage.setX((screen.getWidth() - stage.getWidth()) / 2);
			stage.setY((screen.getHeight() - stage.getHeight()) / 2);
		});
		
		return (T) this;
	}
	
	public void onRootElementChanged(PropertyChangeEvent event)
	{
		Parent newNode = event.getNewValue();
		
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
