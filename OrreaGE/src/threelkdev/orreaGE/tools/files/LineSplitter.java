package threelkdev.orreaGE.tools.files;

public class LineSplitter {
	
	private int pointer = 0;
	private String[] data;
	
	public LineSplitter( String string, String separator ) {
		data = string.split( separator );
	}
	
	public String getNextString() {
		return data[ pointer++ ];
	}
	
	public int getNextInt() {
		return Integer.parseInt( data[ pointer++ ] );
	}
	
	public long getNextLong() {
		return Long.parseLong( data[ pointer++ ] );
	}
	
	public float getNextFloat() {
		return Float.parseFloat( data[ pointer++ ] );
	}
	
	public double getNextDouble() {
		return Double.parseDouble( data[ pointer++ ] );
	}
	
	public boolean getNextBool() {
		return FileUtils.readBoolean( data[ pointer++ ] );
	}
	
	public boolean hasMoreValues() {
		return pointer < data.length;
	}
	
}
