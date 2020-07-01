package ru.swayfarer.swl2.swconf2.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.swayfarer.swl2.string.DynamicString;

@Data 
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SwconfString extends SwconfObject {
	
	public String value;
	
	@Override
	public boolean isString()
	{
		return true;
	}
	
	@Override
	public boolean hasValue()
	{
		return true;
	}
	
	@Override
	public void setRawValue(Object obj)
	{
		this.value = String.valueOf(obj);
	}
	
	@Override
	public String toString(int indent)
	{
		DynamicString ret = new DynamicString();
		
		ret.append("str: ");
		ret.append(value);
		
		return ret.toString();
	}
}
