package ru.swayfarer.swl2.observable.ref;

import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.reference.ParameterizedReference;

/** Ссылка на подписчика {@link IObservable} */
public class ObservableListernerRef<Event_Type> extends ParameterizedReference<IFunction2NoR<ISubscription<Event_Type>, Event_Type>>{

}
