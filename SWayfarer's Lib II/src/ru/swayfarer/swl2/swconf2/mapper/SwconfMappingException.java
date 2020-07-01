package ru.swayfarer.swl2.swconf2.mapper;

@SuppressWarnings("serial")
public class SwconfMappingException extends RuntimeException {

	public SwconfMappingException()
	{
		super();
	}

	public SwconfMappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SwconfMappingException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SwconfMappingException(String message)
	{
		super(message);
	}

	public SwconfMappingException(Throwable cause)
	{
		super(cause);
	}

}
