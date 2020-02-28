package ru.swayfarer.swl2.logger.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.logger.event.LogEvent;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.observable.subscription.ISubscription;
import ru.swayfarer.swl2.resource.file.FileSWL;
import ru.swayfarer.swl2.resource.streams.DataOutputStreamSWL;

/**
 * Писалка логов в файл 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
@AllArgsConstructor(staticName = "of") @Data
public class LogFileHandler implements IFunction2NoR <ISubscription<LogEvent>, LogEvent> {

	/** Файл, в который пишутся логи */
	@InternalElement
	@NonNull
	public FileSWL file;
	
	/** Функция, которая говорит, когда надо сохранять лог */
	@InternalElement
	public IFunction1<LogEvent, Boolean> logConditionFun;
	
	@Override
	public void applyNoR(ISubscription<LogEvent> sub, LogEvent event)
	{
		if (logConditionFun == null || logConditionFun.apply(event))
		{
			file.lock();
			DataOutputStreamSWL stream = file.toAppendStream();
			stream.writeLn(event.logInfo.content);
			stream.closeSafe();
			file.unlock();
		}
	}
	
	/** Задать {@link #logConditionFun} */
	public <T extends LogFileHandler> T setLogCondition(IFunction1<LogEvent, Boolean> logCondition)
	{
		this.logConditionFun = logCondition;
		return (T) this;
	}
}
