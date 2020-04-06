package threelkdev.orreage.tools.lang;

import java.io.File;

import threelkdev.orreage.tools.errors.ErrorManager;

public class GameText {
	
	private static LanguageSheet gameText;
	
	public static void init( File languageFile, int languageId ) {
		try {
			gameText = LanguageSheet.loadFromFile( languageFile, languageId );
		} catch( Exception e ) {
			ErrorManager.crashWithUserAlert( "Language Sheet Error!", "The language sheet could not be loaded for some reason.", e );
		}
	}
	
	public static String get( int id ) {
		return gameText.getText( id );
	}
	
	public static ComplexString getComplex( int id ) {
		return gameText.getComplexText( id );
	}
	
	public static LanguageSheet getLanguageSheet() {
		return gameText;
	}
	
}
