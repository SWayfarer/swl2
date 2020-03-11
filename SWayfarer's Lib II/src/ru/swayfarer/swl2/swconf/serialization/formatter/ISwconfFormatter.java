package ru.swayfarer.swl2.swconf.serialization.formatter;

public interface ISwconfFormatter {

	public void onStart(StringBuilder sb);
    
    public void onEnd(StringBuilder sb);
    
    public void onLiteralStarts(StringBuilder sb);
    
    public void onLiteralEnds(StringBuilder sb);
    
    public void onEqualStarts(StringBuilder sb);
    
    public void onEqualEnds(StringBuilder sb);
    
    public void onBlockEndStarts(StringBuilder sb);
    
    public void onBlockEndEnds(StringBuilder sb);
    
    public void onBlockStartStarts(StringBuilder sb);
    
    public void onBlockStartEnds(StringBuilder sb);
    
    public void onArrayStartStarts(StringBuilder sb);
    
    public void onArrayStartEnds(StringBuilder sb);
    
    public void onArrayEndStarts(StringBuilder sb);
    
    public void onArrayEndEnds(StringBuilder sb);
    
    public void onElementSplitterStarts(StringBuilder sb);
    
    public void onElementSplitterEnds(StringBuilder sb);
    
    public void onNameStarts(StringBuilder sb);
    
    public void onNameEnds(StringBuilder sb);
	
}
