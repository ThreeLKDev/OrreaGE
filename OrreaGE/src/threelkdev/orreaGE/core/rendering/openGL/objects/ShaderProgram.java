package threelkdev.orreaGE.core.rendering.openGL.objects;

import java.io.BufferedReader;
import java.io.File;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import threelkdev.orreaGE.tools.errors.ErrorManager;
import threelkdev.orreaGE.tools.files.FileUtils;

public class ShaderProgram {
	
	private final int programID;
	
	public ShaderProgram( File vertexFile, File fragmentFile, String... attributes ) {
		int vertexShaderID = loadShader( vertexFile, GL20.GL_VERTEX_SHADER );
		int fragmentShaderID = loadShader( fragmentFile, GL20.GL_FRAGMENT_SHADER );
		this.programID = GL20.glCreateProgram();
		GL20.glAttachShader( programID, vertexShaderID );
		GL20.glAttachShader( programID, fragmentShaderID );
		bindAttributes( attributes );
		GL20.glLinkProgram( programID );
		GL20.glDetachShader( programID, vertexShaderID );
		GL20.glDetachShader( programID, fragmentShaderID );
		GL20.glDeleteShader( vertexShaderID );
		GL20.glDeleteShader( fragmentShaderID );
	}
	
	protected void storeAllUniformLocations( Uniform... uniforms ) {
		for( Uniform uniform : uniforms )
			uniform.storeUniformLocation( programID );
		GL20.glValidateProgram( programID );
	}
	
	protected void storeSomeUniformLocations( Uniform... uniforms ) {
		for( Uniform uniform : uniforms )
			uniform.storeUniformLocation( programID );
	}
	
	public void start() {
		GL20.glUseProgram( programID );
	}
	
	public void stop() {
		GL20.glUseProgram( 0 );
	}
	
	public void cleanUp() {
		GL20.glUseProgram( 0 );
		GL20.glDeleteProgram( programID );
	}
	
	private void bindAttributes( String[] attributes ) {
		for( int i = 0 ; i < attributes.length; i++ )
			GL20.glBindAttribLocation( programID, i, attributes[ i ] );
	}
	
	private static int loadShader( File file, int type ) {
		StringBuilder shaderSource = loadSourceCode( file );
		return compileShader( type, shaderSource, file );
	}
	
	private static StringBuilder loadSourceCode( File file ) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = FileUtils.getReader( file );
			String line;
			while ( ( line = reader.readLine() ) != null )
				shaderSource.append( line ).append( "//\n" );
			reader.close();
		} catch ( Exception e ) {
			ErrorManager.crashWithUserAlert( "Shader Error!",
					"There was an error trying to load the following shader: " + file.getPath(), e );
		}
		return shaderSource;
	}
	
	private static int compileShader( int type, StringBuilder shaderSource, File file ) {
		int shaderID = GL20.glCreateShader( type );
		GL20.glShaderSource( shaderID,  shaderSource );
		GL20.glCompileShader( shaderID );
		if( GL20.glGetShaderi( shaderID,  GL20.GL_COMPILE_STATUS ) == GL11.GL_FALSE ) {
			System.out.println( GL20.glGetShaderInfoLog( shaderID, 500 ) );
			System.err.println( "Could not compile shader " + file.getPath() );
			System.exit( -1 );
		}
		return shaderID;
	}
	
}
