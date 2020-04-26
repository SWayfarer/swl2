package ru.swayfarer.swl2.jfx.tags.window;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor(staticName = "of")
public class JfxWindowShowingEvent {

	public boolean isShowing;
	public JfxWindow window;
	
}
