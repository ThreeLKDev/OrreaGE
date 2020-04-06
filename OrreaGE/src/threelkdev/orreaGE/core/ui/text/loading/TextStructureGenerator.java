package threelkdev.orreaGE.core.ui.text.loading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextStructureGenerator {
	
	protected static final int SPACE_ASCII = 32;
	// ?
	
	private final Map< Integer, Character > metaData;
	private final float spaceWidth;
	
	private List< Line > lines;
	private Line currentLine;
	private Word currentWord;
	
	public TextStructureGenerator( Map< Integer, Character > metaData, float spaceWidth ) {
		this.metaData = metaData;
		this.spaceWidth = spaceWidth;
	}
	
	public List< Line > createStructure( String text, float fontSize, float maxLineLength ) {
		char[] chars = text.toCharArray();
		initStructure( fontSize, maxLineLength );
		for( char c : chars ) {
			processChar( c, fontSize, maxLineLength );
		}
		completeStructure( fontSize, maxLineLength );
		return lines;
	}
	
	private void initStructure( float fontSize, float maxLineLength ) {
		this.lines = new ArrayList< Line >();
		this.currentLine = new Line( spaceWidth, fontSize, maxLineLength );
		this.currentWord = new Word( fontSize );
	}
	
	private void processChar( char c, float fontSize, float maxLineLength ) {
		int ascii = ( int ) c;
		if ( ascii == SPACE_ASCII ) {
			moveToNextWord( fontSize, maxLineLength );
		} else {
			addCharToWord( ascii );
		}
	}
	
	private void moveToNextWord( float fontSize, float maxLineLength ) {
		boolean added = currentLine.attemptToAddWord( currentWord );
		if( !added ) {
			lines.add( currentLine );
			currentLine = new Line( spaceWidth, fontSize, maxLineLength );
			currentLine.attemptToAddWord( currentWord );
		}
		currentWord = new Word( fontSize );
	}
	
	private void addCharToWord( int ascii ) {
		Character character = metaData.get( ascii );
		if( character != null ) {
			currentWord.addCharacter( character );
		} else {
			System.err.println( "ERROR CHAR: " + ascii );
		}
	}
	
	private void completeStructure( float fontSize, float maxLineLength ) {
		moveToNextWord( fontSize, maxLineLength );
		lines.add( currentLine );
	}
}
