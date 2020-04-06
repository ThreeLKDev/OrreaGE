package threelkdev.orreage.tools.errors;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Calendar;

public class ErrorManager {
	
	public static final String STANDARD_TITLE = "Orrea: Error";
	public static final String STANDARD_MESSAGE = "An error has caused the program to crash.";
	
	private static File logFolder = new File("DefaultErrorLogs");
	
	public static void init( File folder ) {
		logFolder = folder;
		Thread.setDefaultUncaughtExceptionHandler( new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException( Thread t, Throwable e ) {
				e.printStackTrace();
				crashWithUserAlert( e );
			}
		});
	}
	
	public static void crashWithUserAlert( Throwable error ) {
		crashWithUserAlert( STANDARD_TITLE, STANDARD_MESSAGE, error );
	}
	
	public static void crashWithUserAlert( String title, String message, String error ) {
		ErrorPopUp.showPopUp( title, message, error );
		try {
			createErrorLog( title, message + " - " + error );
			Thread.sleep( 100000000 );
		} catch ( Exception e ) {
			
		}
	}
	
	public static void crashWithUserAlert( String title, String message, Throwable error ) {
		error.printStackTrace();
		crashWithUserAlert( title, message, errorToString( error ) );
	}
	
	public static void createErrorLog( String name, Throwable error ) {
		error.printStackTrace();
		createErrorLog( name, errorToString( error ) );
	}
	
	public static void createErrorLog( String errorType, String message ) {
		try {
			File textFile = createErrorLogFile();
			writeErrorLog( textFile, errorType, message );
		} catch ( Exception e ) {
			System.err.println( "Failed to create error log." );
			e.printStackTrace();
		}
	}
	
	public static String errorToString( Throwable error ) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter( sw );
		error.printStackTrace( pw );
		return sw.toString();
	}
	
	private static File createErrorLogFile() throws Exception {
		File textFile = new File( logFolder, "ERROR" + "_" + Calendar.getInstance().getTime().getTime() + ".txt" );
		if( !logFolder.exists() ) {
			logFolder.mkdir();
		}
		textFile.createNewFile();
		return textFile;
	}
	
	private static void writeErrorLog( File textFile, String name, String contents ) throws Exception {
		PrintWriter printWriter = new PrintWriter( textFile );
		printWriter.println( name );
		printWriter.println( contents );
		printWriter.close();
	}
	
}
