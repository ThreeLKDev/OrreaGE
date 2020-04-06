package threelkdev.orreaGE.core.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import threelkdev.orreaGE.core.ui.text.Text;

public class UiRenderBundle {
	
	private Map< Integer, RenderLevelData > renderData = new HashMap< Integer, RenderLevelData >();
	
	public void addUiRenderData( UiRenderData data ) {
		RenderLevelData levelData = getLevelData( data.getLevel() );
		levelData.addUiRenderData( data );
	}
	
	public void addText( Text text ) {
		RenderLevelData levelData = getLevelData( text.getLevel() );
		levelData.addText( text );
	}
	
	public List< RenderLevelData > getRenderData() {
		List< RenderLevelData > list = new ArrayList< RenderLevelData >();
		for( RenderLevelData levelData : renderData.values() ) {
			sortLevelDataIntoList( list, levelData );
		}
		return list;
	}
	
	private RenderLevelData getLevelData( int level ) {
		RenderLevelData levelData = renderData.get( level );
		if( levelData != null )
			return levelData;
		levelData = new RenderLevelData( level );
		renderData.put( level, levelData );
		return levelData;
	}
	
	private void sortLevelDataIntoList( List< RenderLevelData > list, RenderLevelData levelData ) {
		for( int i = 0; i < list.size(); i++ ) {
			if( levelData.getLevel() < list.get( i ).getLevel() ) {
				list.add( i, levelData );
				return;
			}
		}
		list.add( levelData );
	}
	
}
