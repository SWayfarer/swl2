package ru.swayfarer.swl2.jfx.helpers;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import ru.swayfarer.swl2.observable.IObservable;
import ru.swayfarer.swl2.observable.Observables;

@SuppressWarnings("unchecked")
public class MouseEventsHelper {

	public IObservable<MouseEvent> clicked = Observables.createObservable();
	public IObservable<MouseEvent> released = Observables.createObservable();
	public IObservable<MouseEvent> pressed = Observables.createObservable();
	
	public IObservable<MouseEvent> entered = Observables.createObservable();
	public IObservable<MouseEvent> exited = Observables.createObservable();
	public IObservable<MouseEvent> leave = exited;
	
	public IObservable<MouseEvent> moved = Observables.createObservable();
	
	public IObservable<MouseEvent> dragEntered = Observables.createObservable();
	public IObservable<MouseEvent> dragExited = Observables.createObservable();
	
	public IObservable<MouseEvent> dragReleased = Observables.createObservable();
	
	public IObservable<MouseEvent> dragged = Observables.createObservable();
	
	public <T extends MouseEventsHelper> T init(Node node) 
	{
		node.setOnMouseClicked(clicked::next);
		node.setOnMouseReleased(released::next);
		node.setOnMousePressed(pressed::next);
		
		node.setOnMouseEntered(entered::next);
		node.setOnMouseExited(exited::next);
		node.setOnMouseMoved(moved::next);
		
		node.setOnMouseDragEntered(dragEntered::next);
		node.setOnMouseDragExited(dragExited::next);
		
		node.setOnMouseDragReleased(dragReleased::next);
		
		node.setOnMouseDragged(dragged::next);
		
		return (T) this;
	}
	
	public <T extends MouseEventsHelper> T clear() 
	{
		clicked.clear();
		dragEntered.clear();
		dragExited.clear();
		dragged.clear();
		dragReleased.clear();
		entered.clear();
		exited.clear();
		leave.clear();
		moved.clear();
		pressed.clear();
		released.clear();
		
		return (T) this;
	}
	
}
