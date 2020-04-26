package ru.swayfarer.swl2.jfx.scene.controls;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.jfx.scene.layout.JfxStackedPane;
import ru.swayfarer.swl2.jfx.scene.layout.JfxStackedPane.AlligmentType;
import ru.swayfarer.swl2.markers.ConcattedString;
import ru.swayfarer.swl2.string.StringUtils;

public class TitleBar {

	public Rectangle header = new Rectangle();
	public Label lblTitle = new Label();
	public IExtendedList<Button> buttons = CollectionsSWL.createExtendedList();
	public JfxStackedPane pnButtons = new JfxStackedPane().setAlligment(AlligmentType.HORIZONTAL);
	
	public Button btnClose = new Button("X");
	
	public TitleBar(@ConcattedString Object... title)
	{
		String titleStr = StringUtils.concat(title);
		lblTitle.setText(titleStr);
	}
	
}
