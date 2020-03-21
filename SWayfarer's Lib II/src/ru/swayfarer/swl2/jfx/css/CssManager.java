package ru.swayfarer.swl2.jfx.css;

import java.util.Map;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.collections.streams.DataStream;
import ru.swayfarer.swl2.collections.weak.WeakList;
import ru.swayfarer.swl2.jfx.fxmlwindow.FxmlWindow;
import ru.swayfarer.swl2.markers.InternalElement;
import ru.swayfarer.swl2.resource.rlink.RLUtils;
import ru.swayfarer.swl2.resource.rlink.ResourceLink;

/**
 * Менеджер для работы с css
 * @author swayfarer
 */
public class CssManager {

	/** Экземпляр {@link CssManager}'а для взаимодействия */
	@InternalElement
	public static CssManager instance = new CssManager();
	
	/** Ссылки на css'ки */
	@InternalElement
	public IExtendedList<ResourceLink> cssLinks = CollectionsSWL.createExtendedList();
	
	/** Зарегистрированные слушатели обновлений css для сцен */
	@InternalElement
	public Map<Scene, ICssUpdateListener> registeredCssUpdateHandlers = CollectionsSWL.createWeakMap();
	
	/** Сцены */
	@InternalElement
	public WeakList<Scene> cssScenes = CollectionsSWL.createWeakList();
	
	/** Добавить сцену для обработки через css */
	public synchronized void addScene(Scene scene)
	{
		if (!cssScenes.contains(scene))
		{
			cssScenes.add(scene);
		}
	}
	
	/** Добавить css */
	public synchronized void addCss(ResourceLink node)
	{
		cssLinks.addExclusive(node);
	}

	/** Добавить css */
	public synchronized void addCss(String rlink)
	{
		addCss(RLUtils.createLink(rlink));
	}
	
	/** Зарегистрировать слушателя обновлений css для сцены */
	public synchronized void registerCssUpdateListener(Scene scene, ICssUpdateListener listener)
	{
		registeredCssUpdateHandlers.put(scene, listener);
	}
	
	/** Добавить {@link FxmlWindow}  */
	public synchronized void addWindow(FxmlWindow window)
	{
		Scene scene = window.scene;
		
		if (scene == null)
			return;
		
		if (!cssScenes.contains(scene))
		{
			addScene(scene);
			
			// Удаляем все листнеры, которые были привязаны к старой сцене листнера (т.е. им и были) 
			DataStream.of(registeredCssUpdateHandlers.entrySet())
				.noNull()
				.filter((e) -> e.getValue() == window)
				.each((e) -> registeredCssUpdateHandlers.remove(e.getKey()));
			
			if (window instanceof ICssUpdateListener)
				registerCssUpdateListener(scene, (ICssUpdateListener) window);
		}
	}
	
	/** Применить к сцене */
	public void apply(Scene scene)
	{
		ObservableList<String> stylesheets = scene.getStylesheets();
		stylesheets.clear();
		
		cssLinks.dataStream()
			.noNull()
			.filter(ResourceLink::isExists)
			.each((e) -> stylesheets.add(e.toStream().readAllAsString())
		);
		
		ICssUpdateListener listener = getCssUpdateListener(scene);
		
		if (listener != null)
			listener.onCssUpdated();
	}
	
	/** Получить слушатель обновлений css */
	public ICssUpdateListener getCssUpdateListener(Scene scene)
	{
		return registeredCssUpdateHandlers.get(scene);
	}
	
	/** Применить ко всем сценам  */
	public synchronized void applyAll()
	{
		IExtendedList<String> stylesheetsToAdd = CollectionsSWL.createExtendedList();
		
		cssScenes.dataStream()
			.noNull()
			.each((e) -> {
				ObservableList<String> stylesheets = e.getStylesheets();
				stylesheets.clear();
				stylesheets.addAll(stylesheetsToAdd);
				
				ICssUpdateListener listener = getCssUpdateListener(e);
				
				if (listener != null)
					listener.onCssUpdated();
				}
		);
	}
}
