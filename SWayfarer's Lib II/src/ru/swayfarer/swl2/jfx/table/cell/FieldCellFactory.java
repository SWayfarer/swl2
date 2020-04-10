package ru.swayfarer.swl2.jfx.table.cell;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import ru.swayfarer.swl2.classes.ReflectionUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.reference.IReference;
import ru.swayfarer.swl2.reference.SimpleReference;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FieldCellFactory implements Callback<CellDataFeatures, ObservableValue> {

	public String name;
	
	public IFunction2<String, Object, Object> fieldAccessor = (name, instance) -> ReflectionUtils.getFieldValue(instance, name);
	
	public IObservable<IReference> preSetValue = new SimpleObservable<>();
	
	public TableColumn tableColumn;
	
	public FieldCellFactory(String name, TableColumn tableColumn)
	{
		super();
		this.name = name;
		this.tableColumn = tableColumn;
	}

	public <T extends FieldCellFactory> T setAccessorFunction(IFunction2<String, Object, Object> fun)
	{
		this.fieldAccessor = fun;
		return (T) this;
	}
	
	@Override
	public ObservableValue call(CellDataFeatures var1)
	{
		Object obj = getValueFrom(var1.getValue());
		
		SimpleReference ref = new SimpleReference(obj);
		ref.isSetted = true;
		
		preSetValue.next(ref);
		
		obj = ref.getValue();
		
		return obj == null ? null : new SimpleObjectProperty(obj);
	}
	
	public Object getValueFrom(Object obj)
	{
		return fieldAccessor.process(name, obj);
	}
}
