package ru.swayfarer.swl2.json;

/** 
 * Стадия Json-(де)сериализации
 * @author swayfarer
 *
 */
public enum JsonStage
{
	/** Началась сериалиазация (сохранение) */
	Save_Started,

	/** Завершилась сериалиазация (сохранение) */
	Save_Completed,
	
	/** Началась десериалиазация (загрузка) */
	Load_Started,
	
	/** Завершилась десериалиазация (загрузка) */
	Load_Completed,
}
