package ru.swayfarer.swl2.jfx.scene.controls;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import ru.swayfarer.swl2.jfx.scene.controls.JfxLabel;
import ru.swayfarer.swl2.jfx.scene.controls.JfxText;
import ru.swayfarer.swl2.jfx.scene.layout.JfxAnchorPane;
import ru.swayfarer.swl2.jfx.scene.layout.JfxVBox;
import ru.swayfarer.swl2.jfx.utils.JfxUtils;

@SuppressWarnings("unchecked")
public class JfxInfoDialog extends JfxVBox {

	public JfxText txtContent = new JfxText();
	
	public Header header = new Header();
	
	public JfxInfoDialog(Color color, String header, String content)
	{
		this.header.setColor(color);
		this.header.setText(header);
		
		this.txtContent.setText(content);
		
		addItems(this.header, txtContent);
	}
	
	public static class Header extends JfxAnchorPane {
		
		public JfxLabel lblHeaderText = new JfxLabel();
		public Rectangle rectHeaderBg = new Rectangle();
		
		public Header()
		{
			JfxUtils.fitTo(lblHeaderText, rectHeaderBg);
			
			this.lblHeaderText.setMinHeight(30);
			this.rectHeaderBg.setArcWidth(15);
			this.rectHeaderBg.setArcHeight(15);
			
			addItems(rectHeaderBg, lblHeaderText);
		}
		
		public <T extends Header> T setColor(Color color) 
		{
			this.rectHeaderBg.setFill(color);
			return (T) this;
		}
		
		public <T extends Header> T setText(String text) 
		{
			this.lblHeaderText.setText(text);
			return (T) this;
		}
	}
}
