package ru.swayfarer.swl2.jfx.helpers;

import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;

@SuppressWarnings("unchecked")
public class KeyboardEventsHelper {

	public IObservable<KeyEvent> keyPressed = Observables.createObservable();
	public IObservable<KeyEvent> keyReleased = Observables.createObservable();
	public IObservable<KeyEvent> keyTyped = Observables.createObservable();
	
	public <T extends KeyboardEventsHelper> T init(Node node) 
	{
		node.setOnKeyPressed(keyPressed::next);
		node.setOnKeyReleased(keyReleased::next);
		node.setOnKeyTyped(keyTyped::next);
		
		return (T) this;
	}
	
	public <T extends KeyboardEventsHelper> T clear() 
	{
		keyPressed.clear();
		keyReleased.clear();
		keyTyped.clear();
		
		return (T) this;
	}
}
