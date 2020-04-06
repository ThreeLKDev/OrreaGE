package threelkdev.orreaGE.core.ui.text.loading;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import threelkdev.orreaGE.core.ui.text.Text;
import threelkdev.orreaGE.tools.errors.ErrorManager;
import threelkdev.orreaGE.tools.files.FileUtils;

public class MetaDataLoader {
	
	private static final char QUOTE_CHAR	= '^';
	private static final int QUOTE_REPLACE	=( int ) QUOTE_CHAR;
	private static final int QUOTE_ASCII	= 34;
	private static final int SPACE_ASCII	= 32;
	
	private static final int PAD_TOP	= 0;
	private static final int PAD_LEFT	= 1;
	private static final int PAD_BOTTOM	= 2;
	private static final int PAD_RIGHT	= 3;
	
	private static final int DESIRED_PADDING = 1;
	
	private static final String SPLITTER			= " ";
	private static final String NUMBER_SEPARATOR	= ",";
	
	private float scaleConvertion;
	private int[] padding;
	private int paddingWidth;
	private int paddingHeight;
	
	private float spaceWidth;
	
	private BufferedReader reader;
	private Map< String, String > values = new HashMap< String, String >();
	
	private final File metaFile;
	
	public MetaDataLoader( File metaDataFile ) {
		this.metaFile = metaDataFile;
	}
	
	public TextGenerator loadMetaData() {
		openFile();
		loadPaddingData();
		loadLineSize();
		int imageWidth = getValueOfVariable( "scaleW" );
		Map< Integer, Character > charData = loadCharacterData( imageWidth );
		close();
		return new TextGenerator( new TextStructureGenerator( charData, spaceWidth ), new TextMeshGenerator( spaceWidth ) );
	}
	
	/**
	 * Read in the next line and store the variable value.
	 * @return {@code true} if the end of the file hasn't been reached.
	 */
	private boolean processNextLine() {
		values.clear();
		String line = null;
		try {
			line = reader.readLine();
		} catch ( IOException ioe ) { }
		if( line == null ) return false;
		for( String part : line.split( SPLITTER ) ) {
			String[] valuePairs = part.split( "=" );
			if( valuePairs.length == 2 ) {
				values.put( valuePairs[ 0 ], valuePairs[ 1 ] );
			}
		}
		return true;
	}
	
	/**
	 * Gets the {@code int} value of the variable with a vertain name on the current line.
	 * 
	 * @param variable - The name of the variable.
	 * @return The value of the variable.
	 */
	private int getValueOfVariable( String variable ) {
		return Integer.parseInt( values.get( variable ) );
	}
	
	/**
	 * Gets the array of ints associated with a variable on the current line.
	 * 
	 * @param variable - the name of the variable.
	 * @return The int array of values associated with the variable.
	 */
	private int[] getValuesOfVariable( String variable ) {
		String[] numbers = values.get( variable ).split( NUMBER_SEPARATOR );
		int[] actualValues = new int[ numbers.length ];
		for( int i = 0; i < actualValues.length; i++ ) {
			actualValues[ i ] = Integer.parseInt( numbers[ i ] );
		}
		return actualValues;
	}
	
	/**
	 * Closes the font file after finishing reading.
	 */
	private void close() {
		try {
			reader.close();
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
	}
	
	private void openFile() {
		try {
			reader = FileUtils.getReader( metaFile );
		} catch ( Exception e ) {
			ErrorManager.crashWithUserAlert( "Font Loading Error", "Couldn't load metadata for font.", e );
		}
	}
	
	private void loadPaddingData() {
		processNextLine();
		this.padding = getValuesOfVariable( "padding" );
		this.paddingWidth = padding[ PAD_LEFT ] + padding[ PAD_RIGHT ];
		this.paddingHeight = padding[ PAD_TOP ] + padding[ PAD_BOTTOM ];
	}
	
	private void loadLineSize() {
		processNextLine();
		int lineHeight = getValueOfVariable( "lineHeight" ) - paddingHeight;
		this.scaleConvertion = Text.LINE_HEIGHT_PIXELS / ( float ) lineHeight;
	}
	
	private Map< Integer, Character > loadCharacterData( int imageWidth ) {
		processNextLine();
		processNextLine();
		int count = getValueOfVariable( "count" );
		Map< Integer, Character > charData = new HashMap< Integer, Character >();
		for( int i = 0; i < count; i++ ) {
			processNextLine();
			processNextCharacter( imageWidth, charData );
		}
		return charData;
	}
	
	private void processNextCharacter( int imageSize, Map< Integer, Character> charData ) {
		Character c = loadCharacter( imageSize );
		if( c == null || c.getId() == QUOTE_REPLACE ) 
			return;
		if( c.getId() == QUOTE_ASCII ) 
			charData.put( QUOTE_REPLACE, c );
		charData.put( c.getId(), c );
	}
	
	private Character loadCharacter( int imageSize ) {
		int id = getValueOfVariable( "id" );
		if( id == SPACE_ASCII ) {
			this.spaceWidth = ( getValueOfVariable( "xadvance" ) - paddingWidth ) * scaleConvertion;
			return null;
		}
		double xTex = ( ( double ) getValueOfVariable( "x" ) + ( padding[ PAD_LEFT ] - DESIRED_PADDING ) ) / imageSize;
		double yTex = ( ( double ) getValueOfVariable( "y" ) + ( padding[ PAD_TOP ] - DESIRED_PADDING ) ) / imageSize;
		int width = getValueOfVariable( "width" ) - ( paddingWidth - ( 2 * DESIRED_PADDING ) );
		int height = getValueOfVariable( "height" ) - ( paddingHeight - ( 2 * DESIRED_PADDING ) );
		double xTexSize = ( double ) width / imageSize;
		double yTexSize = ( double ) height / imageSize;
		
		double xOff = ( getValueOfVariable( "xoffset" ) + padding[ PAD_LEFT ] - DESIRED_PADDING ) * scaleConvertion;
		double yOff = ( getValueOfVariable( "yoffset" ) + padding[ PAD_TOP ] - DESIRED_PADDING ) * scaleConvertion;
		double quadWidth = width * scaleConvertion;
		double quadHeight = height * scaleConvertion;
		double xAdvance = ( getValueOfVariable( "xadvance" ) - paddingWidth ) * scaleConvertion;
		return new Character( id, xTex, yTex, xTexSize, yTexSize, xOff, yOff, quadWidth, quadHeight, xAdvance );
	}
	
}
