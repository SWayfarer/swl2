package ru.swayfarer.swl2.jfx.scene.layout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.jfx.scene.controls.JfxCheckbox;
import ru.swayfarer.swl2.jfx.scene.controls.JfxLabel;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.observable.property.ObservableProperty.PropertyChangeEvent;
import ru.swayfarer.swl2.string.StringUtils;

@SuppressWarnings("unchecked")
public class JfxCheckedList extends JfxStackedPane {
	
	public boolean isShowSelection = true;
	
	public Color selectionColor = Color.CADETBLUE;
	
	public ObservableProperty<CheckedListElement> selectedElement = Observables.createProperty();
	public IObservable<CheckEvent> eventCheck = Observables.createObservable();
	public ObservableProperty<Float> fontSize = Observables.createProperty(13f);
	
	public <T extends JfxCheckedList, E> T setListItems(IFunction1<E, String> nameFun, IFunction1<E, Boolean> isCheckedFun, E... content) 
	{
		clear();
		addListItems(nameFun, isCheckedFun, content);
		
		selectedElement.subscribe(this::onSelectedItemChanged);
		
		return (T) this;
	}
	
	public <T extends JfxCheckedList, E> T addListItems(IFunction1<E, String> nameFun, IFunction1<E, Boolean> isCheckedFun, E... content) 
	{
		for (E obj : content)
		{
			addListItem(obj, nameFun.$(obj), isCheckedFun.$(obj));
		}
		
		return (T) this;
	}
	
	public void onSelectedItemChanged(PropertyChangeEvent event)
	{
		CheckedListElement oldItem = event.getOldValue();
		CheckedListElement newItem = event.getNewValue();
	
		if (oldItem != null)
		{
			oldItem.setBackgroundColor(Color.TRANSPARENT);
		}
		
		if (isShowSelection)
		{	
			if (newItem != null)
			{
				newItem.setBackgroundColor(selectionColor);
			}
		}
	}
	
	public <T extends JfxCheckedList> T setShowSelection(boolean isShowSelection) 
	{
		this.isShowSelection = isShowSelection;
		return (T) this;
	}
	
	public <T extends JfxCheckedList> T addListItem(Object obj, String text, boolean isChecked) 
	{
		CheckedListElement elem = new CheckedListElement(obj, fontSize, text);

		elem.eventsMouse.clicked.subscribe((event) -> {
			selectedElement.setValue(elem);
		});
		
		if (isChecked)
			elem.checkBox.setSelected(true);
		
		elem.eventsMouse.clicked.subscribe(() -> {
			selectedElement.setValue(elem);
		});
		
		elem.checkBox.selectedProperty().addListener((property, oldValue, newValue) -> {
			eventCheck.next(CheckEvent.of(elem.content, elem, newValue));
		});
		
//		widthProperty().addListener((p, oldValue, newValue) -> {
//			elem.setPrefWidth(newValue.doubleValue());
//		});
		
		elem.prefWidthProperty().bind(widthProperty());
		
		
		add(elem);
		
		return (T) this;
	}
	
	@EqualsAndHashCode
	public static class CheckedListElement extends JfxHBox{
		public Object content;
		public JfxCheckbox checkBox;
		public JfxLabel lblComment;
		
		public CheckedListElement(Object content, ObservableProperty<Float> fontSize, @ConcattedString Object... text)
		{
			super();
			this.content = content;
			this.checkBox = new JfxCheckbox();
			this.lblComment = new JfxLabel();
			this.lblComment.setHorizontalSizePolicy(Priority.ALWAYS);
			this.checkBox.setHorizontalSizePolicy(Priority.NEVER);
			
			this.lblComment.setHorizontalSizePolicy(Priority.ALWAYS);
			this.lblComment.setContentDisplay(ContentDisplay.CENTER);
			this.lblComment.setTextAlignment(TextAlignment.CENTER);
			this.lblComment.setText(StringUtils.concat(text));
			this.setHorizontalSizePolicy(Priority.ALWAYS); 
			this.checkBox.setHorizontalSizePolicy(Priority.NEVER);
			this.lblComment.setFontScale(fontSize.getFloatValue());
			
			fontSize.subscribe((event) -> this.lblComment.setFontScale(event.getNewValue()));
			
			this.lblComment.setAlignment(Pos.CENTER);
			
			HBox.setMargin(checkBox, new Insets(0, 10, 0, 0));
			
			this.prefWidthProperty().addListener((p, oldValue, newValue) -> {
				this.lblComment.setPrefWidth(newValue.doubleValue() - checkBox.getPrefWidth());
			});
			
			this.addItems(lblComment, checkBox);
			
//			setAlignment(Pos.TOP_RIGHT);
		}
		
		public <T> T getContent()
		{
			return (T) content;
		}
	}
	
	@Data @AllArgsConstructor(staticName = "of")
	public static class CheckEvent {
		public Object item;
		public CheckedListElement element;
		public boolean isChecked;
		
		public <T> T getItem()
		{
			return (T) item;
		}
	}
	
}
