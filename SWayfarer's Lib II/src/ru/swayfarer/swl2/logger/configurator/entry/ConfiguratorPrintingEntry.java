package ru.swayfarer.swl2.logger.configurator.entry;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.SimpleLoggerSWL;
import ru.swayfarer.swl2.logger.decorators.FormattedStacktraceDecorator;
import ru.swayfarer.swl2.string.StringUtils;
import ru.swayfarer.swl2.swconf.serialization.comments.CommentSwconf;

public class ConfiguratorPrintingEntry {

	@CommentSwconf
	(
			  "\nFormat of logging. Use actors: \n"
			+ "%file% - file where logging source located\n"
			+ "%level% - level of log\n"
			+ "%thread% - name of thread from which log was called\n"
			+ "%logger% - name of log's logger\n"
			+ "%text% - text of log message\n"
			+ "%from% - line and source file simple name (e.g. SomeFile.java:123) from which log was called\n"
			+ "%fromFull% - line and source file full name (e.g. some.path.to.SomeFile.java:123) from which log was called\n"
	)
	public String format = SimpleLoggerSWL.defaultFormat;
	
	@CommentSwconf("Stacktrace element format")
	public String stacktraceFormat = FormattedStacktraceDecorator.defaultStacktraceFormat;
	
	@CommentSwconf("If sets to true output colors will be hidden")
	public boolean hideColors = false;

	@CommentSwconf("Throwables decorator seq")
	public String decoratorSeq = "=-";
	
	@CommentSwconf("All stacktrace elements that starts with element of this list will be not shown")
	public IExtendedList<String> stacktraceBlocks = CollectionsSWL.createExtendedList();
	
	public void applyToLogger(ILogger logger)
	{
		if (!StringUtils.isEmpty(format))
			logger.setLogFormat(format);
		
		if (!CollectionsSWL.isNullOrEmpty(stacktraceBlocks))
			logger.addStackstraceBlocker(stacktraceBlocks.toArray(String.class));
		
		if (!StringUtils.isEmpty(stacktraceFormat))
			logger.setStacktraceElementFormat(stacktraceFormat);
		
		if (hideColors)
			logger.hideColors();
		
		if (!StringUtils.isEmpty(decoratorSeq))
		{
			logger.setDecoratorSeq(decoratorSeq);
		}
	}
}
