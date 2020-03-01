package ru.swayfarer.swl2.options;

import lombok.ToString;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1NoR;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.string.StringUtils;

/** Опция */
@SuppressWarnings("unchecked")
@ToString
public class Option {

	/** Имя */
	public String name;
	
	/** Значение */
	public String value;
	
	/** Описание */
	public String description;
	
	/** Имеет ли значение? */
	public boolean hasValue;
	
	/** Функция, которая будет вызвана во время применения опции  */
	public IFunction1NoR<Option> applyOptionFun;
	
	/** Обязательная ли опиця? */
	public boolean isRequired;
	
	/** Применить функцию */
	public void apply()
	{
		if (applyOptionFun != null)
			applyOptionFun.apply(this);
	}
	
	/** Обязательная ли опиця? */
	public boolean isRequied()
	{
		return isRequired;
	}
	
	/** Добавить значение из текста */
	public void loadValue(String text)
	{
		if (StringUtils.isEmpty(value))
			value = text;
		else
			value += " " + text;
	}
	
	/** Задать имя */
	public <T extends Option> T setName(@ConcattedString Object... name)
	{
		this.name = StringUtils.concat(name);
		return (T) this;
	}
	
	/** Задать описание */
	public <T extends Option> T setDescription(@ConcattedString Object... description)
	{
		this.description = StringUtils.concat(description);
		return (T) this;
	}

	/** Задать {@link #hasValue} */
	public <T extends Option> T setValueEnabled(boolean isEnabled)
	{
		this.hasValue = isEnabled;
		return (T) this;
	}
	
	/** Задать {@link #applyOptionFun} */
	public <T extends Option> T setInitFun(IFunction1NoR<Option> fun)
	{
		this.applyOptionFun = fun;
		return (T) this;
	}
	
	public <T extends Option> T setRequired(boolean isRequired)
	{
		this.isRequired = isRequired;
		return (T) this;
	}
	
}
