package ru.swayfarer.swl2.jfx.table.cell;

import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;

@SuppressWarnings({"rawtypes", "unchecked"})
public class TextTableCell {

	public static <Table_Item_Type> IObservable<TableCellChangeEvent<Table_Item_Type, ?>> injectToColumn (
		TableColumn<Table_Item_Type, ?> wrappedColumn, String propertyName, 
		IFunction1<String, Boolean> validator, 
		IFunction1<Object, Object> cellValueFun
	)
	{
		IObservable ret = new SimpleObservable<>();
		
		TableColumn tcl = wrappedColumn;
		
		tcl.setCellValueFactory(new FieldCellFactory(propertyName, tcl));
		
		Callback factory = TextFieldTableCell.forTableColumn();
		
		tcl.setCellFactory((a) -> {
			
			TextFieldTableCell cell = (TextFieldTableCell) factory.call(a);
			
			AtomicBoolean isInEvent = new AtomicBoolean();
			
			cell.textProperty().addListener((value, oldValue, newValue) -> {
				if (isInEvent.get())
					return;
				
				isInEvent.set(true);
				
				if (!validator.$(newValue))
					cell.textProperty().set(oldValue);
				
				isInEvent.set(false);
			});
			
			return cell;
		});
		
		tcl.setOnEditCommit(new EventHandler<CellEditEvent>()
		{
			@Override
			public void handle(CellEditEvent event)
			{
				TableCellChangeEvent<Object, Object> evt = TableCellChangeEvent.of(event.getRowValue(), event.getOldValue(), event.getNewValue());
				ret.next(evt);
				
				if (!evt.isCanceled())
				{
					ReflectionUtils.setFieldValue(evt.item, cellValueFun.apply(evt.newValue), propertyName);
				}
			}
		});
		
		return ret;
	}
}
