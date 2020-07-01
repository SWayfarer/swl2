package ru.swayfarer.swl2.generics;

@SuppressWarnings("serial")
public class GenericParseException extends RuntimeException {

	public GenericParseException()
	{
		super();
	}

	public GenericParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GenericParseException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public GenericParseException(String message)
	{
		super(message);
	}

	public GenericParseException(Throwable cause)
	{
		super(cause);
	}

}
