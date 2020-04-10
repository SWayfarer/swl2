package ru.swayfarer.swl2.input;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;

public class Mouse {

	public Mouse()
	{
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            public void eventDispatched(AWTEvent event) {
            	
            	
            	System.out.println("Event");
                if(event instanceof MouseEvent){
                    MouseEvent evt = (MouseEvent)event;
                    if(evt.getID() == MouseEvent.MOUSE_CLICKED){
                        System.out.println("mouse clicked at: " + evt.getPoint());
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
	}
	
}
