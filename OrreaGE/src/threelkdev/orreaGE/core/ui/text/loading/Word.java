package threelkdev.orreaGE.core.ui.text.loading;

import java.util.ArrayList;
import java.util.List;

/**
 * During the loading of a text this represents one word in the text.
 * @author Karl
 *
 */
public class Word {
	
	private final double fontSize;
	
	private List< Character > characters = new ArrayList< Character >();
	private double pixelWidth = 0;
	
	/**
	 * Create a new empty word.
	 * @param fontSize - the font size of the text which this word is in.
	 */
	protected Word( double fontSize ) {
		this.fontSize = fontSize;
	}
	
	/**
	 * Adds a character to the end of the current word and increases the pixel width of the word.
	 * @param character - the character to be added.
	 */
	protected void addCharacter( Character character ) {
		characters.add( character );
		pixelWidth += character.getXAdvance() * fontSize;
	}
	
	/**
	 * @return The list of characters in the word.
	 */
	protected List< Character > getCharacters() {
		return characters;
	}
	
	/**
	 * @return The width of the word in terms of pixels;
	 */
	protected double getWordWidthPixels() { return pixelWidth; }
	
	protected int getCharCount() { return characters.size(); }
	
	@Override
	public String toString() {
		String s = "";
		for( Character c : characters ) {
			s += ( char ) c.getId();
		}
		return s;
	}
}
