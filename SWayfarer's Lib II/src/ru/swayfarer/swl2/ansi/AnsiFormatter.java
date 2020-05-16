package ru.swayfarer.swl2.ansi;

import java.util.Map;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;

/**
 * Форматтер ANSI-цветов
 * <br> Форматирует префиксы формата &{code} в ANSI-префиксы, зарегистрированные через {@link #registerColor(String, String)} 
 * @author swayfarer
 *
 */
@SuppressWarnings("unchecked")
public class AnsiFormatter {

	/** Форматтер */
	@InternalElement
	public static AnsiFormatter instance = new AnsiFormatter();
	
	/** Зарегистрированные цветовае коды */
	@InternalElement
	public Map<String, String> foregroundColorCodes = CollectionsSWL.createHashMap();
	
	/** Зарегистрированные цветовае коды */
	@InternalElement
	public Map<String, String> backgroundColorCodes = CollectionsSWL.createHashMap();
	
	/** Зарегистрированные цветовае коды */
	@InternalElement
	public Map<String, String> styleCodes = CollectionsSWL.createHashMap();
	
	/** Регулярка, по которой находятся цветовые префиксы */
	@InternalElement
	public static String ANSI_FORMAT_PREFIX = StringUtils.regex()
			.text("&{")
			.not("&", "{", "}")
			.text("}")
			.build();
	
	/** Конструктор */
	public AnsiFormatter()
	{
		registerDefaultCodes();
	}
	
	/** Регистрация стандартных кодов */
	@InternalElement
	public void registerDefaultCodes()
	{
		this.foregroundColorCodes.put("black", "0");
		this.foregroundColorCodes.put("red", "1");
		this.foregroundColorCodes.put("green", "2");
		this.foregroundColorCodes.put("yellow", "3");
		this.foregroundColorCodes.put("blue", "4");
		this.foregroundColorCodes.put("magnetta", "5");
		this.foregroundColorCodes.put("cyan", "6");
		this.foregroundColorCodes.put("white", "7");
		
		this.styleCodes.put("normal", "15");
		this.styleCodes.put("no", "15");
		this.styleCodes.put("italic", "3");
		this.styleCodes.put("border", "1");
		this.styleCodes.put("underlined", "4");
	}
	
	/** 
	 * Форматировать текст 
	 * @param text Форматируемый текст
	 * @return Форматированный текст 
	 */
	public String format(String text)
	{
		IExtendedList<String> coloredPrefixes = StringUtils.getAllMatches(ANSI_FORMAT_PREFIX, text);
		
		if (!CollectionsSWL.isNullOrEmpty(coloredPrefixes))
		{
			IExtendedList<String> colors = CollectionsSWL.createExtendedList();
			
			for (String colorPrefix : coloredPrefixes)
			{
				String colorPrefixWithoutSpaces = colorPrefix.replace(" ", "");
				
				StringBuilder totalColor = new StringBuilder();
				
				if (colorPrefixWithoutSpaces.startsWith("&{h:"))
				{
					String id = StringUtils.subString(4, -1, colorPrefixWithoutSpaces);
					
					if (StringUtils.isInteger(id))
					{
						int i = Integer.valueOf(id);
						
						if (i > 0 && i < colors.size())
						{
							totalColor.append(colors.get(i));
						}
					}
				}
				else if (colorPrefixWithoutSpaces.length() > 3)
				{
					String[] codes = StringUtils.subString(2, -1, colorPrefixWithoutSpaces).split(",");
					
					String foreground = codes.length > 0 ? codes[0] : null;
					String background = codes.length > 1 ? codes[1] : null;
					String style = codes.length > 2 ? codes[2] : null;
					
					totalColor.append(getColor(foreground, background, style));
				}
				else
				{
					totalColor.append(Ansi.SANE);
				}
				
				if (totalColor.length() != 0)
				{
					colors.add(0, totalColor.toString());
				}
				
				text = text.replace(colorPrefix, totalColor);
			}
		}
		
		return text;
	}
	
	/** 
	 * Получить цвет со значением по-умолчанию 
	 * @param code Код цвета
	 */
	public String getColor(String code, String background, String style)
	{
//		if (StringUtils.isInteger(code))
//			return "\033[2;38;5;" + code + "m";

		DynamicString colorPrefix = new DynamicString();

		if (!StringUtils.isEmpty(background))
		{
			colorPrefix.append("\033[");
			colorPrefix.append(getForegroundColor(background));
			colorPrefix.append("m");
		}

		colorPrefix.append("\033[");

		if (StringUtils.isEmpty(style))
		{
			style = "15";
		}
		
		colorPrefix.append(getStyle(style));
		colorPrefix.append(";38;5;");

		if (!StringUtils.isEmpty(code))
		{
			colorPrefix.append(getForegroundColor(code));
			colorPrefix.append("m");
		}
		
		return colorPrefix.toString();
	}

	public String getForegroundColor(String code)
	{
		String ret = foregroundColorCodes.get(code);
		return ret == null ? code : ret;
	}
	
	public String getStyle(String code)
	{
		String ret = styleCodes.get(code);
		return ret == null ? code : ret;
	}
	
	/** 
	 * Зарегистрировать цвет по его коду
	 * @param code Код цвета
	 * @param color Цветовая ANSI-вставка, соответствующая коду 
	 */
	public <T extends AnsiFormatter> T registerColor(String code, String color)
	{
		this.foregroundColorCodes.put(code, color);
		return (T) this;
	}
}
