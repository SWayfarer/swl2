package ru.swayfarer.swl2.swconf2.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.swayfarer.swl2.string.DynamicString;
import ru.swayfarer.swl2.string.StringUtils;

@Data 
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SwconfNum extends SwconfObject{

	public Double value;
	
	@Override
	public boolean isNum()
	{
		return false;
	}
	
	@Override
	public boolean hasValue()
	{
		return true;
	}
	
	@Override
	public void setRawValue(Object obj)
	{
		this.value = ((Number) obj).doubleValue();
	}
	
	@Override
	public String toString(int indent)
	{
		DynamicString ret = new DynamicString();
		
		ret.append("num: ");
		ret.append(value);
		
		return ret.toString();
	}
}
