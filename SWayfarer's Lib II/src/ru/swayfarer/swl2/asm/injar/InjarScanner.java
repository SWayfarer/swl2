package ru.swayfarer.swl2.asm.injar;

/** 
 * {@link InJarTransformer} с выключенной записью 
 * @author swayfarer
 */
public class InjarScanner extends InJarTransformer {

	/** Конструктор */
	public InjarScanner()
	{
		this.setReadonly(true);
	}
	
}
