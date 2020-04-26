package ru.swayfarer.swl2.jfx.tags.window;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.observable.IObservableList;
import ru.swayfarer.swl2.jfx.scene.controls.JfxImageView;
import ru.swayfarer.swl2.jfx.scene.controls.JfxText;
import ru.swayfarer.swl2.jfx.scene.layout.JfxAnchorPane;
import ru.swayfarer.swl2.jfx.scene.layout.JfxHBox;
import ru.swayfarer.swl2.jfx.utils.JfxIconFactory;
import ru.swayfarer.swl2.jfx.utils.JfxUtils;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.observable.property.ObservableProperty.ChangeEvent;
import ru.swayfarer.swl2.string.StringUtils;

@SuppressWarnings("unchecked")
public class JfxTitleDecorator extends JfxAnchorPane {

	public JfxIconFactory iconManager = JfxIconFactory.instance;
	
	public ObservableProperty<Color> fillColor = Observables.createProperty();
	
	public JfxImageView imgIcon = new JfxImageView();
	public JfxText titleText;
	public Rectangle rectBg = new Rectangle();
	
	public JfxImageView btnClose = new JfxImageView(iconManager.closeIconImage.get());
	public JfxImageView btnMaximize = new JfxImageView(iconManager.unMaximizeIconImage.get());
	public JfxImageView btnHide = new JfxImageView(iconManager.hideIconImage.get());
	
	public JfxHBox decoratorContentBox = new JfxHBox();
	public JfxHBox buttonsBox = new JfxHBox(btnHide, btnMaximize, btnClose);
	
	public IObservableList<JfxImageView> buttons = CollectionsSWL.createObservableList();
	
	public JfxWindow window;
	
	public AtomicBoolean isClicked = new AtomicBoolean();
	
	public AtomicInteger lastPressX = new AtomicInteger();
	public AtomicInteger lastPressY = new AtomicInteger();
	
	public JfxTitleDecorator(JfxWindow window, @ConcattedString Object... text)
	{
		this.window = window;
		fillColor.setPost(true);
		titleText = new JfxText(StringUtils.concat(text));
		
		titleText.setFill(Color.WHITE);
		
		JfxUtils.fitTo(this, rectBg);
		
		buttons.evtPostUpdate().subscribe((e) -> {
			buttonsBox.getChildren().setAll(buttons);
		});
		
		btnClose.eventsMouse.clicked.subscribe(() -> {
			window.close();
		});
		
		btnMaximize.eventsMouse.clicked.subscribe(() -> {
			window.setMaximized(!window.isMaximized());
		});
		
		btnHide.eventsMouse.clicked.subscribe(() -> {
			window.setIconified(!window.isIconified());
		});
		
		buttonsBox.setSpacing(7);
		buttonsBox.setHorizontalSizePolicy(Priority.ALWAYS);
		buttonsBox.setAlignment(Pos.TOP_RIGHT);
		
		decoratorContentBox.setAlignment(Pos.CENTER_LEFT);
		buttonsBox.setAlignment(Pos.CENTER_RIGHT);
		
		decoratorContentBox.addItems(imgIcon, this.titleText, buttonsBox);

		imgIcon.setMarigins(10, 0, 0, 0);
		titleText.setMarigins(15, 5, 0, 5);
		
		JfxUtils.fitTo(this, decoratorContentBox);
		
		addItems(rectBg, decoratorContentBox);
		
		fillColor.subscribe((event) -> rectBg.setFill(event.getNewValue()));
		
		btnClose.eventsMouse.entered.subscribe(() -> {
			btnClose.setImage(iconManager.closeIconImageHighlighted.get());
		});
		
		btnClose.eventsMouse.exited.subscribe(() -> {
			btnClose.setImage(iconManager.closeIconImage.get());
		});
		
		btnMaximize.eventsMouse.entered.subscribe(() -> {
			btnMaximize.setImage(iconManager.unMaximizeIconImageHighlighted.get());
		});
		
		btnMaximize.eventsMouse.exited.subscribe(() -> {
			btnMaximize.setImage(iconManager.unMaximizeIconImage.get());
		});
		
		btnHide.eventsMouse.entered.subscribe(() -> {
			btnHide.setImage(iconManager.hideIconImageHighlighted.get());
		});
		
		btnHide.eventsMouse.exited.subscribe(() -> {
			btnHide.setImage(iconManager.hideIconImage.get());
		});
		
		eventsMouse.pressed.subscribe((event) -> {
			
			lastPressX.set((int) event.getSceneX());
			lastPressY.set((int) event.getSceneY());
			
			isClicked.set(true);
		});
		
		eventsMouse.dragExited.subscribe(() -> {
			isClicked.set(false);
		});
		
		eventsMouse.dragged.subscribe((event) -> {
			
			if (isClicked.get())
			{
				window.stage.get().setX(event.getScreenX() - lastPressX.get());
				window.stage.get().setY(event.getScreenY() - lastPressY.get());
			}
		});
		
		fillColor.setValue(Color.rgb(34, 45, 50));
		
		setMinHeight(35);
	}
	
	@Override
	public void setWidth(double value)
	{
		// TODO Auto-generated method stub
		super.setWidth(value);
	}
	
	public <T extends JfxTitleDecorator> T setIcon(Image icon) 
	{
		this.imgIcon.setImage(icon);
		return (T) this;
	}
	
	public <T extends JfxTitleDecorator> T setText(@ConcattedString Object... text) 
	{
		this.titleText.setText(StringUtils.concat(text));
		return (T) this;
	}
	
	public <T extends JfxTitleDecorator> T addButton(JfxImageView btn) 
	{
		buttons.add(btn);
		return (T) this;
	}
	
	public void onFillColorChaged(ChangeEvent event)
	{
		Color newColor = event.getNewValue();
		rectBg.setFill(newColor);
	}
}
