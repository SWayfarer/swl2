package ru.swayfarer.swl2.jfx.wrappers;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.markers.InternalElement;

@SuppressWarnings("unchecked")
public class JfxTableWrapper<Table_Item> implements IFxmlWrapper{

	@InternalElement
	public TableView<Table_Item> wrappedTable;
	
	public JfxTableWrapper(TableView<Table_Item> tbl)
	{
		this.wrappedTable = tbl;
	}
	
	public Table_Item getSelectedItem()
	{
		return wrappedTable.getSelectionModel().getSelectedItem();
	}
	
	public <T extends JfxTableWrapper<Table_Item>> T setEditable(boolean isEditable) 
	{
		wrappedTable.setEditable(isEditable);
		return (T) this;
	}
	
	public void clear()
	{
		wrappedTable.getItems().clear();
	}
	
	public <Element_Type> void setAll(Iterable<Element_Type> sourceElements, IFunction1<? super Element_Type, ? extends Table_Item> transformer)
	{
		clear();
		ObservableList<Table_Item> items = wrappedTable.getItems();
		
		for (Element_Type elem : sourceElements)
		{
			items.add(transformer.apply(elem));
		}
		
		wrappedTable.refresh();
	}
}
