package threelkdev.orreage.tools.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtils {
	
	public static enum ResourceType {
		SHADER( "shaders" ),
		MODEL( "models" ),
		TEXTURE( "textures" ),
		SOUND( "sounds" ),
		OTHER( "other" );
		
		private String name;
		ResourceType( String name ){
			this.name = name;
		}
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	public static final String SEPARATOR = ";";
	public static final String FILE_SEPARATOR = "/";
	public static final int TRUE = 1;
	public static final int FALSE = 0;
	
	private static Map< ResourceType, List< File > > registeredResourcePaths = new HashMap< ResourceType, List< File > >();
	
	public static void registerPath( ResourceType type, String path ) { registerPath( type, new File( path ) ); }
	public static void registerPath( ResourceType type, File path ) {
		if( registeredResourcePaths.containsKey( type ) )
			registeredResourcePaths.get( type ).add( path );
		else {
			registeredResourcePaths.put( type, new ArrayList< File >() );
			registeredResourcePaths.get( type ).add( path );
		}
	}
	
	public static boolean readBoolean( String value ) {
		int boolValue = Integer.parseInt( value );
		return boolValue == TRUE;
	}
	
	public static int booleanToInt( boolean bool ) {
		return bool ? TRUE : FALSE;
	}
	
	public static void closeBufferedReader( BufferedReader reader ) {
		try {
			reader.close();
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
	}
	
	public static BufferedReader getReader( File file ) throws Exception {
		try {
			BufferedReader reader = new BufferedReader( new FileReader( file ) );
			return reader;
		} catch ( Exception e ) {
			System.err.println( "Couldn't get reader for " + file.getPath() );
			throw e;
		}
	}
	
	public static File getShader( String filename ) { return getResource( ResourceType.SHADER, filename ); }
	public static File getModel( String filename ) { return getResource( ResourceType.MODEL, filename ); }
	public static File getTexture( String filename ) { return getResource( ResourceType.TEXTURE, filename ); }
	public static File getSound( String filename ) { return getResource( ResourceType.SOUND, filename ); }
	public static File getResource( String filename ) { return getResource( ResourceType.OTHER, filename ); }
	public static File getResource( ResourceType type, String filename ) {
		File result = null;
		result = getLocalResource( type, filename );
		if( result == null ) {
			result = getRegisteredResource( type, filename );
		}
		return result;	
	}
	
	private static File getLocalResource( ResourceType type, String filename ) {
		File out = new File( "./res/the3lks/" + type.toString() + "/" + filename );
		if( out.exists() )
			return out;
		else return null;
	}
	
	private static File getRegisteredResource( ResourceType type, String filename ) {
		File out;
		if( registeredResourcePaths.containsKey( type ) ) {
			for( File path : registeredResourcePaths.get( type ) ) {
				out = new File( path, filename );
				if( out.exists() )
					return out;
			}
		}
		return null;
	}
	/*

Pass in FileUtils.SHADER, magic/fire.vertex.glsl
	-> search Local first:
		/res/shader/magic/fire.vertex.glsl
	-> if null then try registered res folders
		registered SHADER folders: the3lks/res/shader, the3lks/LL/res/shader
			the3lks/res/shader/magic/fire.vertex.glsl
			the3lks/LL/res/shader/magic/fire.vertex.glsl


	 */
}
