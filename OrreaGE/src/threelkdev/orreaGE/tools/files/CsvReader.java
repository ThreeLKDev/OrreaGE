package threelkdev.orreaGE.tools.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class CsvReader {
	
	private final String separator;
	private BufferedReader reader;
	private LineSplitter splitter;
	
	@SuppressWarnings("resource")
	public static CsvReader open( File file ) throws Exception {
		BufferedReader reader = FileUtils.getReader( file );
		return new CsvReader( reader, FileUtils.SEPARATOR );
	}
	
	public CsvReader( BufferedReader reader, String separator ) throws Exception {
		this.reader = reader;
		this.separator = separator;
	}
	
	public String nextLine() {
		String line = null;
		try {
			line = reader.readLine();
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
		if( line == null ) {
			return null;
		}
		this.splitter = new LineSplitter( line, separator );
		return line;
	}
	
	public String getNextLabelString() {
		getNextString();
		return getNextString();
	}
	
	public float getNextLabelFloat() {
		getNextString();
		return getNextFloat();
	}
	
	public int getNextLabelInt() {
		getNextString();
		return getNextInt();
	}
	
	public int[] getNextLabelIntArray() {
		getNextString();
		return getNextIntArray();
	}
	
	public float[] getNextLabelFloatArray() {
		getNextString();
		return getNextFloatArray();
	}
	
	public int[] getNextIntArray() {
		int count = getNextInt();
		int[] array = new int[ count ];
		for( int i = 0; i < count; i++ ) {
			array[ i ] = getNextInt();
		}
		return array;
	}
	
	public float[] getNextFloatArray() {
		int count = getNextInt();
		float[] array = new float[ count ];
		for( int i = 0; i < count; i++ ) {
			array[ i ] = getNextFloat();
		}
		return array;
	}
	
	public String getNextString() {
		return splitter.getNextString();
	}
	
	public int getNextInt() {
		return splitter.getNextInt();
	}
	
	public long getNextLong() {
		return splitter.getNextLong();
	}
	
	public float getNextFloat() {
		return splitter.getNextFloat();
	}
	
	public boolean isEndOfLine() {
		return !splitter.hasMoreValues();
	}
	
	public boolean getNextBool() {
		return splitter.getNextBool();
	}
	
	public Matrix4f getNextMatrix4f() {
		Matrix4f result = new Matrix4f();

		result.m00 = splitter.getNextFloat();
		result.m01 = splitter.getNextFloat();
		result.m02 = splitter.getNextFloat();
		result.m03 = splitter.getNextFloat();
		
		result.m10 = splitter.getNextFloat();
		result.m11 = splitter.getNextFloat();
		result.m12 = splitter.getNextFloat();
		result.m13 = splitter.getNextFloat();
		
		result.m20 = splitter.getNextFloat();
		result.m21 = splitter.getNextFloat();
		result.m22 = splitter.getNextFloat();
		result.m23 = splitter.getNextFloat();
		
		result.m30 = splitter.getNextFloat();
		result.m31 = splitter.getNextFloat();
		result.m32 = splitter.getNextFloat();
		result.m33 = splitter.getNextFloat();
		
		return result;
	}
	
	public Vector4f getNextVector4f() {
		float x = splitter.getNextFloat();
		float y = splitter.getNextFloat();
		float z = splitter.getNextFloat();
		float w = splitter.getNextFloat();
		return new Vector4f( x, y, z, w );
	}
	
	public Vector3f getNextVector3f() {
		float x = splitter.getNextFloat();
		float y = splitter.getNextFloat();
		float z = splitter.getNextFloat();
		return new Vector3f( x, y, z );
	}
	
	public Vector2f getNextVector2f() {
		float x = splitter.getNextFloat();
		float y = splitter.getNextFloat();
		return new Vector2f( x, y );
	}
	
	public byte[] getNextByteArray( int size ) {
		byte[] array = new byte[ size ];
		for( int i = 0; i < array.length; i++ ) {
			int value = getNextInt();
			if( value < 0 ) {
				return null;
			}
			array[ i ] = ( byte ) value;
		}
		return array;
	}
	
	public void close() {
		FileUtils.closeBufferedReader( reader );
	}
}
