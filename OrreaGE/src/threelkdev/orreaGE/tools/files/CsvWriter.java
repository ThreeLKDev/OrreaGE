package threelkdev.orreaGE.tools.files;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CsvWriter {
	
	private final String separator;
	private PrintWriter writer;
	private boolean first = true;
	
	@SuppressWarnings("resource")
	public static CsvWriter open( File file ) throws Exception {
		PrintWriter writer = new PrintWriter( new FileWriter( file ) );
		return new CsvWriter( writer, FileUtils.SEPARATOR );
	}
	
	public CsvWriter( PrintWriter writer, String separator ) {
		this.separator = separator;
		this.writer = writer;
	}
	
	public void writeValue( String value ) {
		if( !first ) {
			writer.print( separator );
		}
			first = false;
			writer.print( value );
	}
	
	public void writeInt( int i ) {
		writeValue( Integer.toString( i ) );
	}
	
	public void writeFloat( float f ) {
		writeValue( Float.toString( f ) );
	}
	
	public void writeBoolean( boolean bool ) {
		writeInt( FileUtils.booleanToInt( bool ) );
	}
	
	public void nextLine() {
		writer.println();
		first = true;
	}
	
	public void close() {
		writer.close();
	}
	
}
