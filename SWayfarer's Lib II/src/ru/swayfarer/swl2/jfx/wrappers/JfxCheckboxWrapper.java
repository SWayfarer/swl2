package ru.swayfarer.swl2.jfx.wrappers;

import javafx.scene.control.CheckBox;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.markers.Alias;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.SimpleObservable;
import ru.swayfarer.swl2.observable.property.ObservableProperty;
import ru.swayfarer.swl2.observable.ref.ObservableListernerRef;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.recursive.RecursiveBreaker;

/**
 * Обертка над {@link CheckBoxBuilder}'ом 
 * @author swayfarer
 */
@SuppressWarnings("unchecked")
public class JfxCheckboxWrapper implements IFxmlWrapper {

	/** Контейнер для безопасного от рекурсии запуска задач */
	@InternalElement
	public RecursiveBreaker recursiveBreaker = new RecursiveBreaker();
	
	/** Обернутый {@link CheckBoxBuilder} */
	@InternalElement
	public CheckBox wrappedBox;
	
	/** Событие изменения значения чекбокса */
	@InternalElement
	public IObservable<Boolean> checkEvent = new SimpleObservable<>();
	
	/** Ссылка на обработчик изменения значения чекбокса для обеспечения привязки */
	@InternalElement
	public ObservableListernerRef<Boolean> activeAttachCheckboxFunRef = new ObservableListernerRef<>();
	
	/** Подписка на событие изменения проперти, к которой прикреплен {@link CheckBox}. Хранится для отписки при перепривязке. */
	@InternalElement
	public ISubscription<?> activeAttachPropertySubscription;
	
	/** Конструктор */
	public JfxCheckboxWrapper(CheckBox wrappedBox)
	{
		ExceptionsUtils.IfNullArg(wrappedBox, "Wrapped box can't be null!");
		this.wrappedBox = wrappedBox;
		wrappedBox.selectedProperty().addListener((observable, oldValue, newValue) -> checkEvent.next(newValue));
		checkEvent.subscribe(0, activeAttachCheckboxFunRef);
	}
	
	/** Отмечен ли чекбокс? */
	public boolean isChecked()
	{
		return wrappedBox.isSelected();
	}
	
	/** Привязать к проперти */
	public <T extends JfxCheckboxWrapper> T attachTo(ObservableProperty<Boolean> propery)
	{
		return attachTo(propery, (b) -> b, (b) -> (Boolean) b);
	}
	
	/** Привязать к пропети, указав функции преобразования значений чекбокса и проперти */
	public <Property_Type, T extends JfxCheckboxWrapper> T attachTo(ObservableProperty<Property_Type> propery, IFunction1<Boolean, ? extends Property_Type> valueMapper, IFunction1<Property_Type, Boolean> checkMapper)
	{
		// Если подписка на проперти уже есть, то ее надо удалить 
		
		if (activeAttachPropertySubscription != null && !activeAttachPropertySubscription.isDisposed())
			activeAttachPropertySubscription.dispose();
		
		setChecked(checkMapper.apply(propery.getValue()));
		
		activeAttachPropertySubscription = propery.subscribe((listener) -> { 
			recursiveBreaker.run(() -> wrappedBox.setSelected(checkMapper.apply(listener.getNewValue())));
		});
		
		activeAttachCheckboxFunRef.setValue((sub, isChecked) -> {
			recursiveBreaker.run(() -> propery.setValue(valueMapper.apply(isChecked())));
		});
		
		return (T) this;
	}
	
	/** Задать значение */
	public <T extends JfxCheckboxWrapper> T setChecked(boolean isChecked)
	{
		wrappedBox.setSelected(isChecked);
		return (T) this;
	}
	
	/** Добавить обработчик изменения значения */
	public ISubscription<?> addCheckHandler(IFunction1NoR<Boolean> fun)
	{
		return checkEvent.subscribe(fun);
	}
	
	/** Добавить обработчик изменения значения */
	@Alias("addCheckHandler")
	public ISubscription<?> subscribe(IFunction1NoR<Boolean> fun)
	{
		return addCheckHandler(fun);
	}
	
}
