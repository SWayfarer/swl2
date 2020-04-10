package ru.swayfarer.swl2.jfx.table.cell;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.jfx.table.cell.BooleanTableCheckboxCell.CheckboxEvent.EventType;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.string.StringUtils;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BooleanTableCheckboxCell  extends TableCell {
	
	public static ILogger logger = LoggingManager.getLogger();
	
	@Getter
	public CheckBox checkBox;

	public IFunction1<Object, Boolean> boxValueFun;
	
	public IObservable<CheckboxEvent> selectEvent;
	
	public boolean isUpdating;
	
	@Getter
	public int rowIndex;
	
	public BooleanTableCheckboxCell(IFunction1<Object, Boolean> boxValueFun)
	{
		checkBox = new CheckBox();
		
		checkBox.selectedProperty().addListener((listener) -> {
			onChecked(checkBox.isSelected());
		});
		
		this.rowIndex = getIndex();
		
		this.boxValueFun = boxValueFun;
		
		this.setGraphic(checkBox);
		this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
	}
	
	public boolean hasRow()
	{
		return getTableRow() != null;
	}

	@Override
	public void startEdit()
	{
		super.startEdit();
		if (isEmpty())
		{
			return;
		}
		checkBox.setDisable(false);
		checkBox.requestFocus();
	}

	@Override
	public void cancelEdit()
	{
		super.cancelEdit();
	}

	public void onChecked(boolean isChecked)
	{
		CheckboxEvent event = CheckboxEvent.builder()
				.cell(this)
				.type(isUpdating ? EventType.REFRESH : EventType.CHANGE)
				.build();
		
		
		selectEvent.next(event);
	}

	@Override
	public void updateItem(Object item, boolean empty)
	{
		checkBox.setVisible(item != null);
		
		isUpdating = true;
		super.updateItem(item, empty);
		
		if (item != null && !isEmpty())
		{
			try
			{
				checkBox.setSelected(boxValueFun.apply(item));
			}
			catch (Throwable e)
			{
				logger.error(e, "Error while updating checkbox value by fun", boxValueFun);
			}
		}
		
		isUpdating = false;
	}
	
	public static IObservable<CheckboxEvent> injectToColumn(TableColumn column, String name)
	{
		return injectToColumn(column, name, null, null);
	}
	
	public static IObservable<CheckboxEvent> injectToColumn(TableColumn column, String name, IFunction1<Boolean, Object> itemValueFun, IFunction1<Object, Boolean> boxValueFun)
	{
		if (boxValueFun == null)
			boxValueFun = (itemValue) -> (Boolean) itemValue;
		
		if (itemValueFun == null)
			itemValueFun = (isChecked) -> isChecked;
			
		Factory factory = Factory.builder()
				.boxValueFun(boxValueFun)
				.build();
		
		column.setCellFactory(factory);
		column.setCellValueFactory(new FieldCellFactory(name, column));
		
		if (!StringUtils.isEmpty(name))
		{
			factory.creationEvent.subscribe(new SelectionHandler(name, itemValueFun));
		}
		
		return factory.creationEvent;
	}
	
	public static class SelectionHandler implements IFunction1NoR<CheckboxEvent> {
		
		public String fieldName;
		public IFunction1<Boolean, Object> itemValueFun;
		
		public SelectionHandler(String fieldName, IFunction1<Boolean, Object> itemValueFun)
		{
			super();
			this.fieldName = fieldName;
			this.itemValueFun = itemValueFun;
		}

		@Override
		public void applyNoR(CheckboxEvent event)
		{
			if (event.type == EventType.CHANGE)
			{
				BooleanTableCheckboxCell cell = event.cell;
				
				Object obj = event.cell.getTableView().getItems().get(cell.getIndex());
				Object value = itemValueFun.apply(event.cell.checkBox.isSelected());
				
				ReflectionUtils.setFieldValue(obj, value, fieldName);
			}
		}
	}
	
	@Builder
	public static class Factory implements Callback {

		public IFunction1<Object, Boolean> boxValueFun;
		
		@Builder.Default
		public IObservable<CheckboxEvent> creationEvent = new SimpleObservable<>();
		
		@Override
		public Object call(Object var1)
		{
			BooleanTableCheckboxCell ret = new BooleanTableCheckboxCell(boxValueFun);
			ret.setAlignment(Pos.CENTER);
			ret.selectEvent = creationEvent;
			
			CheckboxEvent checkboxEvent = CheckboxEvent.builder()
					.type(CheckboxEvent.EventType.CREATE)
					.cell(ret)
					.build();
			
			creationEvent.next(checkboxEvent);
			
			return ret;
		}
	}
	
	@Data @Builder @AllArgsConstructor
	public static class CheckboxEvent {
		
		public EventType type;
		public BooleanTableCheckboxCell cell;
		
		public <T> T getTableItem()
		{
			return (T) cell.getTableView().getItems().get(getRowIndex());
		}
		
		public int getRowIndex()
		{
			return cell.getTableRow().getIndex();
		}
		
		public boolean isChecked()
		{
			return cell.checkBox.isSelected();
		}
		
		public static enum EventType {
			
			CREATE,
			REFRESH,
			CHANGE
			
		}
		
	}
}
