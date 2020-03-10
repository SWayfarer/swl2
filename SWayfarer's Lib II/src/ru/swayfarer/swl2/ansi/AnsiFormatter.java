package ru.swayfarer.swl2.ansi;

import java.util.Map;

import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.markers.InternalElement;
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
	public Map<String, String> colorCodes = CollectionsSWL.createHashMap();
	
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
		this.colorCodes.put("no", Ansi.SANE);
		this.colorCodes.put("bk", Ansi.BLACK);
		this.colorCodes.put("rd", Ansi.RED);
		this.colorCodes.put("gr", Ansi.GREEN);
		this.colorCodes.put("yl", Ansi.YELLOW);
		this.colorCodes.put("bl", Ansi.BLUE);
		this.colorCodes.put("mg", Ansi.MAGENTA);
		this.colorCodes.put("cy", Ansi.CYAN);
		this.colorCodes.put("wh", Ansi.WHITE);
		
		this.colorCodes.put("bg_bk", Ansi.BACKGROUND_BLACK);
		this.colorCodes.put("bg_rd", Ansi.BACKGROUND_RED);
		this.colorCodes.put("bg_gr", Ansi.BACKGROUND_GREEN);
		this.colorCodes.put("bg_yl", Ansi.BACKGROUND_YELLOW);
		this.colorCodes.put("bg_bl", Ansi.BACKGROUND_BLUE);
		this.colorCodes.put("bg_mg", Ansi.BACKGROUND_MAGENTA);
		this.colorCodes.put("bg_cy", Ansi.BACKGROUND_CYAN);
		this.colorCodes.put("bg_wh", Ansi.BACKGROUND_WHITE);
	}
	
	/** Форматировать текст */
	public String format(String text)
	{
		IExtendedList<String> coloredPrefixes = StringUtils.getAllMatches(ANSI_FORMAT_PREFIX, text);
		
		if (!CollectionsSWL.isNullOrEmpty(coloredPrefixes))
		{
			for (String colorPrefix : coloredPrefixes)
			{
				String colorPrefixWithoutSpaces = colorPrefix.replace(" ", "");
				
				StringBuilder totalColor = new StringBuilder();
				
				if (colorPrefixWithoutSpaces.length() > 3)
				{
					String[] codes = StringUtils.subString(2, -1, colorPrefixWithoutSpaces).split(",");
					String color = null;
					
					for (String code : codes)
					{
						color = getColor(code);
						
						if (color != null)
							totalColor.append(color);
					}
				}
				else
				{
					totalColor.append(Ansi.SANE);
				}
				
				if (totalColor.length() != 0)
				{
					text = text.replace(colorPrefix, totalColor);
				}
			}
		}
		
		return text;
	}
	
	/** Получить цвет по коду */
	public String getColor(String code)
	{
		return getColor(code, null);
	}
	
	/** Получить цвет со значением по-умолчанию */
	public String getColor(String code, String defaultColor)
	{
		if (StringUtils.isInteger(code))
			return "\u001B[" + code + "m";
		
		String ret = colorCodes.get(code);
		return ret == null ? defaultColor : ret;
	}

	
	/** Зарегистрировать цвет по его коду */
	public <T extends AnsiFormatter> T registerColor(String code, String color)
	{
		this.colorCodes.put(code, color);
		return (T) this;
	}
}
