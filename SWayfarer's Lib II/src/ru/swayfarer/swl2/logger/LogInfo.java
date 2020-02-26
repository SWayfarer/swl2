package ru.swayfarer.swl2.logger;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.exceptions.ExceptionsUtils;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.string.StringUtils;

@Data @AllArgsConstructor(staticName = "of")
@SuppressWarnings("unchecked")
public class LogInfo {

	public StackTraceElement[] callTrace;
	public String content;
	public ILogLevel level;
	public String threadName;
	public long time;
	public boolean isFormatted;
	public ILogger logger;
	public boolean isDecorated;
	
	public static LogInfo of(ILogger logger, ILogLevel level, int stacktraceOffset, @ConcattedString Object... text)
	{
		return of (
				ExceptionsUtils.getThreadStacktrace(
						ExceptionsUtils.StacktraceOffsets.OFFSET_CALLER + stacktraceOffset),
				StringUtils.concatWithSpaces(text),
				level,
				Thread.currentThread().getName(),
				System.currentTimeMillis(),
				false,
				logger,
				false
		);
	}
	
	public <T extends LogInfo> T setDecorated(boolean isDecorated)
	{
		this.isDecorated = isDecorated;
		return (T) this;
	}
}
