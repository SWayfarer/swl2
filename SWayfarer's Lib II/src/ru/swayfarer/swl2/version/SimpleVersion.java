package ru.swayfarer.swl2.version;

import ru.swayfarer.swl2.logger.ILogger;
import ru.swayfarer.swl2.logger.LoggingManager;
import ru.swayfarer.swl2.string.StringUtils;

public class SimpleVersion implements Comparable<SimpleVersion> {

	public int major, minor, build;

	public static final ILogger logger = LoggingManager.getLogger();

	public SimpleVersion(int major, int minor, int build)
	{
		super();
		this.major = major;
		this.minor = minor;
		this.build = build;
	}
	
	public int getMajor()
	{
		return major;
	}

	public void setMajor(int major)
	{
		this.major = major;
	}

	public int getMinor()
	{
		return minor;
	}

	public void setMinor(int minor)
	{
		this.minor = minor;
	}

	public int getBuild()
	{
		return build;
	}

	public void setBuild(int build)
	{
		this.build = build;
	}

	public static SimpleVersion valueOf(String versionStr)
	{
		if (StringUtils.isEmpty(versionStr))
			return null;
		
		int major = 0, minor = 0, build = 0;

		try
		{
			String[] ver = versionStr.split("\\.");

			if (ver != null && ver.length > 0)
			{
				if (ver.length > 0)
				{
					major = Integer.valueOf(ver[0]);
				}

				if (ver.length > 1)
				{
					major = Integer.valueOf(ver[1]);
				}

				if (ver.length > 2)
				{
					build = Integer.valueOf(ver[2]);
				}
			}
		}
		catch (Throwable e)
		{
			logger.error(e, "Error while reading version from", versionStr);

			return null;
		}

		return new SimpleVersion(major, minor, build);
	}
	
	@Override
	public int compareTo(SimpleVersion o)
	{
		if (o == null)
			return 1;
		
		if (major > o.major)
			return 1;
		else if (major == o.major)
		{
			if (minor > o.minor)
				return 1;
			else if (minor == o.minor)
			{
				if (build > o.build)
					return 1;
				else if (build == o.build)
					return 0;
			}
		}
		
		return -1;
	}
	
	public int compareTo(String o)
	{
		return compareTo(valueOf(o));
	}
	
	@Override
	public String toString()
	{
		return major+"."+minor+"."+build;
	}

}

