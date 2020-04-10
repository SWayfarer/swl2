package ru.swayfarer.swl2.jfx.table.cell;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.observable.events.AbstractCancelableEvent;

@Data @AllArgsConstructor(staticName = "of")
@SuppressWarnings("unchecked")
public class TableCellChangeEvent<Table_Item_Type, Value_Type> extends AbstractCancelableEvent {

	public Table_Item_Type item;
	public Value_Type oldValue;
	public Value_Type newValue;
	
	public <T> T getOldValue()
	{
		return (T) oldValue;
	}
	
	public <T> T getNewValue()
	{
		return (T) newValue;
	}
}
