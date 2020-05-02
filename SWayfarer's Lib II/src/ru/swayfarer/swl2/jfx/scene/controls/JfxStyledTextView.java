package ru.swayfarer.swl2.jfx.scene.controls;

import java.util.ArrayList;

import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.swayfarer.swl2.collections.CollectionsSWL;
import ru.swayfarer.swl2.collections.extended.IExtendedList;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction1;
import ru.swayfarer.swl2.functions.GeneratedFunctions.IFunction2NoR;
import ru.swayfarer.swl2.jfx.scene.layout.JfxStackedPane;
import ru.swayfarer.swl2.string.StringUtils;

@SuppressWarnings("unchecked")
public class JfxStyledTextView extends JfxStackedPane {

	public ConsoleInputParse parseInput = new ConsoleInputParse();
	
	public void append(String text)
	{
		add(parseInput.parseInputToArray(text));
	}
	
	public <T extends JfxStyledTextView> T addCustomizer(IFunction1<String, Boolean> isAcceptsFun, IFunction2NoR<String, CustomizeEvent> customizerFun) 
	{
		parseInput.customizers.add(TextCustomizer.of(isAcceptsFun, new TextCustommizerFun()
		{
			@Override
			public void customize(CustomizeEvent event)
			{
				customizerFun.apply(event.text, event);
			}
		}));
		return (T) this;
	}
	
	public <T extends JfxStyledTextView> T addCustomizer(IFunction1<String, Boolean> isAcceptsFun, IFunction1<String, IExtendedList<Text>> customizerFun) 
	{
		parseInput.customizers.add(TextCustomizer.of(isAcceptsFun, customizerFun));
		return (T) this;
	}
	
	public static class ConsoleInputParse {

	    private String[] wordList = {};
	  
	    public IExtendedList<TextCustomizer> customizers = CollectionsSWL.createExtendedList();

	    public ConsoleInputParse() {}

	    public FlowPane parseInputToArray(String input) {
	        wordList = input.trim().split("[ ]+");

	        return colorize();
	    }

	    public FlowPane colorize() {

	        ArrayList<Text> textChunks = new ArrayList<>();
	        FlowPane bundle = new FlowPane();

	        //Todo: use regex to check for valid words
	        for (String word : wordList) {
	        	
	        	boolean isCustomized = false;
	        	
	        	for (TextCustomizer customizer : customizers)
	        	{
	        		if (customizer.isAcceptsFun.apply(word))
	        		{
	        			isCustomized = true;
	        			textChunks.addAll(customizer.customizerFun.apply(word));
	        		}
	        	}
	        	
	        	if (!isCustomized)
	        	{
	        		textChunks.add(new Text(word));
	        	}
	        	
	        	textChunks.add(new Text(" "));
	        	
//	            String spaced = word + " ";
//	            switch (word) {
//	                case "Hello": case "hello":
//	                    textChunks.add(customize(spaced, "purple"));
//	                    break;
//	                case "World": case "world":
//	                    textChunks.add(customize(spaced, "blue"));
//	                    break;
//	                case "Stack Overflow":
//	                    textChunks.add(customize(spaced, "orange", "Arial Bold", 15));
//	                default:
//	                    textChunks.add(customize(spaced, "black", "Arial",  13));
//	                    break;
//	            }
	        }

	        bundle.getChildren().addAll(textChunks);
	        return bundle;
	    }

	    public Text customize(String word, String color) {
	        return customize(word, color, null);
	    }

	    public Text customize(String word, String color, String font) {
	    	return customize(word, color, font, 12);
	    }

	    public Text customize(String word, String color, String font, int fontSize) {
	    	Text text = new Text(word);
	    	
	    	if (!StringUtils.isEmpty(color))
	    		text.setFill(Paint.valueOf(color));
	    	
	    	if (!StringUtils.isEmpty(font))
	    		text.setFont(Font.font(font, fontSize));
	    	
	        return text;
	    }

	}
	
	@AllArgsConstructor(staticName = "of") @Data
	public static class TextCustomizer {
		public IFunction1<String, Boolean> isAcceptsFun = (s) -> false;
		public IFunction1<String, IExtendedList<Text>> customizerFun = TextCustommizerFun.instance;
	}
	
	public static abstract class TextCustommizerFun implements IFunction1<String, IExtendedList<Text>> {

		public static TextCustommizerFun instance = new TextCustommizerFun() {
			@Override public void customize(CustomizeEvent event) {}
		};
		
		@Override
		public IExtendedList<Text> apply(String str)
		{
			CustomizeEvent event = CustomizeEvent.of(null, null, str);
			customize(event);
			
			Text ret = new Text();
			
			if (event.font != null)
			{
				ret.setFont(event.font);
			}
			
			if (event.color != null)
			{
				ret.setFill(event.color);
			}
			
			if (!StringUtils.isEmpty(event.text))
			{
				ret.setText(event.text);
			}
			
			return CollectionsSWL.createExtendedList(ret);
		}
		
		public abstract void customize(CustomizeEvent event);
	}
	
	@Data @AllArgsConstructor(staticName = "of")
	public static class CustomizeEvent {
		public Color color;
		public Font font;
		public String text;
	}
	
}
