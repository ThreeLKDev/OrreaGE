package threelkdev.orreaGE.core.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import threelkdev.orreaGE.core.ui.text.Font;
import threelkdev.orreaGE.core.ui.text.Text;

public class RenderLevelData {
	
	private int level;
	private Collection< UiRenderData > uiElements = new ArrayList< UiRenderData >();
	private Map< Font, List< Text> > texts = new HashMap< Font, List< Text > >();
	//TODO maybe order texts by size, when using multiple maps
	
	protected RenderLevelData( int level ) {
		this.level = level;
	}
	
	protected int getLevel() { return level; }
	public Collection< UiRenderData > getUiElements() { return uiElements; }
	public Map< Font, List< Text > > getTexts() { return texts; }
	
	public void addUiRenderData( UiRenderData data ) {
		uiElements.add( data );
	}
	
	public void addText( Text text ) {
		List< Text > textList = texts.get( text.getFont() );
		if( textList == null ) {
			textList = new ArrayList< Text >();
			texts.put( text.getFont(), textList );
		}
		textList.add( text );
	}
	
}
