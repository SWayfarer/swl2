package ru.swayfarer.swl2.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.swayfarer.swl2.markers.ConcattedString;

public class DynamicString implements CharSequence {
	
	public StringBuilder sb = new StringBuilder();

	public DynamicString() {
		sb = new StringBuilder();
    }

	public DynamicString(int capacity) {
		sb = new StringBuilder(capacity);
    }

	public DynamicString(String str) {
		sb = new StringBuilder(str);
    }
	
	public boolean isEmpty()
	{
		return length() == 0;
	}

	public DynamicString(CharSequence seq) {
        sb = new StringBuilder(seq);
    }
	
	//123
	public DynamicString subString(int start, int end)
	{
		return new DynamicString(sb.subSequence(start, end));
	}

	public DynamicString(@ConcattedString Object... text)
	{
		this(StringUtils.concat(text));
	}
	
	public DynamicString append(@ConcattedString Object... text)
	{
		this.append(StringUtils.concat(text));
		return this;
	}
	
	public DynamicString append(Object obj)
	{
		return append(String.valueOf(obj));
	}

	
	public DynamicString append(String str)
	{
		sb.append(str);
		return this;
	}

	public DynamicString append(StringBuffer sb)
	{
		sb.append(sb);
		return this;
	}

	
	public DynamicString append(CharSequence s)
	{
		sb.append(s);
		return this;
	}

	public DynamicString append(CharSequence s, int start, int end)
	{
		sb.append(s, start, end);
		return this;
	}

	
	public DynamicString append(char[] str)
	{
		sb.append(str);
		return this;
	}

	public DynamicString append(char[] str, int offset, int len)
	{
		sb.append(str, offset, len);
		return this;
	}

	
	public DynamicString append(boolean b)
	{
		sb.append(b);
		return this;
	}

	
	public DynamicString append(char c)
	{
		sb.append(c);
		return this;
	}

	
	public DynamicString append(int i)
	{
		sb.append(i);
		return this;
	}

	
	public DynamicString append(long lng)
	{
		sb.append(lng);
		return this;
	}

	
	public DynamicString append(float f)
	{
		sb.append(f);
		return this;
	}

	public DynamicString append(double d)
	{
		sb.append(d);
		return this;
	}

	public DynamicString appendCodePoint(int codePoint)
	{
		sb.appendCodePoint(codePoint);
		return this;
	}

	public DynamicString delete(int start, int end)
	{
		sb.delete(start, end);
		return this;
	}

	public DynamicString deleteCharAt(int index)
	{
		sb.deleteCharAt(index);
		return this;
	}

	public DynamicString replace(int start, int end, String str)
	{
		sb.replace(start, end, str);
		return this;
	}

	public DynamicString insert(int index, char[] str, int offset, int len)
	{
		sb.insert(index, str, offset, len);
		return this;
	}

	public DynamicString insert(int offset, Object obj)
	{
		sb.insert(offset, obj);
		return this;
	}

	public DynamicString insert(int offset, String str)
	{
		sb.insert(offset, str);
		return this;
	}

	public DynamicString insert(int offset, char[] str)
	{
		sb.insert(offset, str);
		return this;
	}

	public DynamicString insert(int dstOffset, CharSequence s)
	{
		sb.insert(dstOffset, s);
		return this;
	}

	public DynamicString insert(int dstOffset, CharSequence s, int start, int end)
	{
		sb.insert(dstOffset, s, start, end);
		return this;
	}

	public DynamicString insert(int offset, boolean b)
	{
		sb.insert(offset, b);
		return this;
	}

	public DynamicString insert(int offset, char c)
	{
		sb.insert(offset, c);
		return this;
	}

	public DynamicString insert(int offset, int i)
	{
		sb.insert(offset, i);
		return this;
	}
	
	public boolean endsWith(CharSequence suffix) {
        return startsWith(suffix, length() - suffix.length());
    }
	
	public boolean contains(CharSequence s) {
        return indexOf(s.toString()) > -1;
    }
	
	public boolean startsWith(CharSequence prefix, int toffset) {
        int to = toffset;
        int po = 0;
        int pc = prefix.length();
        
        // Note: toffset might be near -1>>>1.
        if ((toffset < 0) || (toffset > length() - pc)) {
            return false;
        }
        
        while (--pc >= 0) {
            if (charAt(to++) != prefix.charAt(po++)) {
                return false;
            }
        }
        return true;
    }

	public DynamicString insert(int offset, long l)
	{
		sb.insert(offset, l);
		return this;
	}

	public DynamicString insert(int offset, float f)
	{
		sb.insert(offset, f);
		return this;
	}

	public DynamicString insert(int offset, double d)
	{
		sb.insert(offset, d);
		return this;
	}
	
	public int indexOf(String str)
	{
		return sb.indexOf(str);
	}
	
	public int indexOf(String str, int fromIndex)
	{
		return sb.indexOf(str, fromIndex);
	}
	
	public int lastIndexOf(String str)
	{
		return sb.lastIndexOf(str);
	}
	
	public int lastIndexOf(String str, int fromIndex)
	{
		return sb.lastIndexOf(str, fromIndex);
	}
	
	public DynamicString reverse()
	{
		sb.reverse();
		return this;
	}
	
	public DynamicString replace(String oldString, String newString)
	{
		return replaceByRegex(StringUtils.regex().text(oldString).build(), newString, -1);
	}
	
	public DynamicString replaceFirst(String oldString, String newString)
	{
		return replaceByRegex(StringUtils.regex().text(oldString).toString(), newString, 1);
	}
	
	public DynamicString setText(@ConcattedString Object... text)
	{
		String str = StringUtils.concat(text);
		
		sb.replace(0, sb.length(), str);
		
		return this;
	}
	
	public DynamicString replaceAll(String oldStringRegex, String newString)
	{
		return replaceByRegex(oldStringRegex, newString, -1);
	}
	
	public DynamicString replaceByRegex(String oldStringRegex, String newString, int maxReplacementsCount)
	{
		Pattern pattern = Pattern.compile(oldStringRegex);
		
		Matcher matcher = pattern.matcher(sb);
		
		boolean isInfiniteReplacements = maxReplacementsCount < 0;
		int replacementsCount = 0;
		
		while (matcher.find())
		{
			if (!isInfiniteReplacements && replacementsCount >= maxReplacementsCount)
				break;
			
			sb.replace(matcher.start(), matcher.end(), newString);
			replacementsCount ++;
			
			matcher = pattern.matcher(sb);
		}
		
		return this;
	}

	public String toString()
	{
		return sb.toString();
	}

	@Override
	public int length()
	{
		return sb.length();
	}
	
	public DynamicString clear()
	{
		sb.delete(0, length());
		return this;
	}

	@Override
	public char charAt(int index)
	{
		return sb.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end)
	{
		return sb.subSequence(start, end);
	}

}
