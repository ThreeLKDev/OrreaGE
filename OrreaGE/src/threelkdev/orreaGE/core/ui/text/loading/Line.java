package threelkdev.orreaGE.core.ui.text.loading;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line of text during the loading of a text.
 * 
 * @author Karl
 *
 */
public class Line {
	
	private final double maxPixelLength;
	private final double spacePixelWidth;
	
	private List< Word > words = new ArrayList< Word >();
	private double lineLengthPixels = 0;
	
	private int characterCount = 0;
	
	/**
	 * Creates an empty line.
	 * 
	 * @param spaceWidth
	 *            - the pixel width of a space character.
	 * @param fontSize
	 *            - the size of font being used.
	 * @param maxLength
	 *            - the maximum length of a line in pixels.
	 */
	protected Line( double spaceWidth, double fontSize, double maxLength ) {
		this.spacePixelWidth = spaceWidth * fontSize;
		this.maxPixelLength = maxLength;
	}
	
	/**
	 * Attempt to add a word to the line. If the line can fit the word in
	 * without reaching the maximum line length then the word is added and the
	 * line length increased.
	 * 
	 * @param word
	 *            - the word to try to add.
	 * @return {@code true} if the word has successfully been added to the line.
	 */
	protected boolean attemptToAddWord( Word word ) {
		double additionalLength = word.getWordWidthPixels();
		additionalLength += !words.isEmpty() ? spacePixelWidth : 0;
		if( lineLengthPixels + additionalLength <= maxPixelLength ) {
			words.add( word );
			characterCount += word.getCharCount();
			lineLengthPixels += additionalLength;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @return The max length of the line in pixels.
	 */
	protected double getMaxPixelLength() { return maxPixelLength; }
	
	/**
	 * @return The current pixel length of the line.
	 */
	protected double getPixelLength() { return lineLengthPixels; }
	
	/**
	 * @return The list of words in the line.
	 */
	protected List< Word > getWords() { return words; }
	
	protected int getCharCount() { return characterCount; }
	
}
