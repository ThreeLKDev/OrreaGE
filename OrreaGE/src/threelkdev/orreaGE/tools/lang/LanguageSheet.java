package threelkdev.orreaGE.tools.lang;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import threelkdev.orreaGE.tools.files.CsvReader;

public class LanguageSheet {
	
	private final Language[] availableLanguages;
	private final Language currentLanguage;
	private final Map< Integer, String > strings;
	
	protected static LanguageSheet loadFromFile( File languageFile, int initialLanguage ) throws Exception {
		CsvReader reader = CsvReader.open( languageFile );
		Language[] languages = loadLanguages( reader );
		Map< Integer, String > data = loadLanguageData( reader, initialLanguage );
		return new LanguageSheet( data, initialLanguage, languages );
	}
	
	private LanguageSheet( Map< Integer, String > strings, int languageId, Language[] languages ) {
		this.strings = strings;
		this.availableLanguages = languages;
		this.currentLanguage = languages[ languageId ];
	}
	
	public Language getCurrentLanguage() {
		return currentLanguage;
	}
	
	public String getText( int id ) {
		return strings.get( id );
	}
	
	public ComplexString getComplexText( int id ) {
		return new ComplexString( getText( id ) );
	}
	
	public Language[] getAvailableLanguages() {
		return availableLanguages;
	}
	
	private static Map< Integer, String > loadLanguageData( CsvReader reader, int languageId ){
		Map< Integer, String > strings = new HashMap< Integer, String >();
		while( reader.nextLine() != null ) {
			readInLine( reader, languageId, strings );
		}
		return strings;
	}
	
	private static void readInLine( CsvReader reader, int languageId, Map< Integer, String > strings ) {
		int id = reader.getNextInt();
		reader.getNextString();
		String defaultString = reader.getNextString();
		String languageString = getLanguageString( reader, languageId );
		if( languageString == null || languageString.isEmpty() ) {
			languageString = defaultString;
		}
		strings.put( id,  languageString );
	}
	
	private static String getLanguageString( CsvReader reader, int languageId ) {
		String translation = null;
		for( int i = 0; i < languageId; i++ ) {
			if( reader.isEndOfLine() ) {
				return null;
			}
			translation = reader.getNextString();
		}
		return translation;
	}
	
	private static Language[] loadLanguages( CsvReader reader ) {
		reader.nextLine();
		reader.getNextString();
		reader.getNextString();
		List< Language > languages = new ArrayList< Language >();
		int id = 0;
		while( !reader.isEndOfLine() ) {
			languages.add( new Language( reader.getNextString(), id++ ) );
		}
		return languages.toArray( new Language[ languages.size() ] );
	}
}
