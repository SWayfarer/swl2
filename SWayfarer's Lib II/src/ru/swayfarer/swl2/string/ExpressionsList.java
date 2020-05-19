package ru.swayfarer.swl2.string;

import ru.swayfarer.swl2.collections.extended.ExtendedListWrapper;
import ru.swayfarer.swl2.markers.ConcattedString;

@SuppressWarnings("unchecked")
public class ExpressionsList extends ExtendedListWrapper<String>{

	public boolean isMatches(@ConcattedString Object... text)
	{
		for (String str : this)
		{
			if (StringUtils.isMatchesByExpression(str, text))
				return true;
		}
		
		return false;
	}
	
	public <T extends ExpressionsList> T addMask(@ConcattedString Object... text)
	{
		add("mask:" + StringUtils.concat(text));
		return (T) this;
	}
	
	public <T extends ExpressionsList> T addRegex(@ConcattedString Object... text)
	{
		add("regex:" + StringUtils.concat(text));
		return (T) this;
	}
	
}
