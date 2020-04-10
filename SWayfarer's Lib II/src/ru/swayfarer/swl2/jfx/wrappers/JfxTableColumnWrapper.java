package ru.swayfarer.swl2.jfx.wrappers;

import javafx.scene.control.TableColumn;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.jfx.table.cell.BooleanTableCheckboxCell;
import ru.swayfarer.swl2.jfx.table.cell.BooleanTableCheckboxCell.CheckboxEvent;
import ru.swayfarer.swl2.jfx.table.cell.TableCellChangeEvent;
import ru.swayfarer.swl2.jfx.table.cell.TextTableCell;
import ru.swayfarer.swl2.observable.IObservable;

public class JfxTableColumnWrapper<Table_Item, Column_Content> implements IFxmlWrapper {

	public TableColumn<Table_Item, Column_Content> wrappedColumn;

	public JfxTableColumnWrapper(TableColumn<Table_Item, Column_Content> wrappedColumn)
	{
		super();
		this.wrappedColumn = wrappedColumn;
	}

	public IObservable<CheckboxEvent> setCheckboxMode(String propertyName)
	{
		return BooleanTableCheckboxCell.injectToColumn(wrappedColumn, propertyName);
	}
	
	public IObservable<TableCellChangeEvent<Table_Item, Column_Content>> setTextMode(String propertyName)
	{
		return setTextMode(propertyName, (s) -> true);
	}
	
	public IObservable<TableCellChangeEvent<Table_Item, Column_Content>> setTextMode(String propertyName, IFunction1<String, Boolean> validator)
	{
		return ReflectionUtils.forceCast(TextTableCell.injectToColumn(wrappedColumn, propertyName, validator, (s) -> s));
	}
	
//	public IObservable<TableCellChangeEvent<Table_Item, Column_Content>> setDoubleMode(String propertyName)
//	{
//		return ReflectionUtils.forceCast(TextTableCell.injectToColumn(wrappedColumn, propertyName, StringUtils::isDouble, (s) -> Double.valueOf(s + "")));
//	}

}
