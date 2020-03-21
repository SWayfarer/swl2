package ru.swayfarer.swl2.jfx.css;

/**
 * Слушатель обновлений Css
 * @author swayfarer
 *
 */
public interface ICssUpdateListener {

	/**
	 *  Вызывается, когда css сцены перезагружаются (т.е. затираются и записываются по-новой)
	 * <br> Для добавления кастомных css
	 */
	public void onCssUpdated();
	
}
