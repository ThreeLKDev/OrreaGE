package threelkdev.orreaGE.core.rendering.objects;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import threelkdev.orreaGE.core.engine.Orrea;
import threelkdev.orreaGE.core.inputs.Mouse;
import threelkdev.orreaGE.core.inputs.MouseButton;
import threelkdev.orreaGE.core.windows.Window;
import threelkdev.orreaGE.tools.utils.SmoothFloat;

/**
 * Represents the in-game camera. This class is in charge of keeping the
 * projection-view-matrix updated. It allows the user to alter the pitch and yaw
 * with the left mouse button.
 * 
 * NOTE: Optimizations and other miscellaneous edits made by The3lKs
 * 
 * @author Karl, The3LKs
 *
 */
public class Camera implements ICamera {
	
	private static final float PITCH_SENSITIVITY = 150f;
	private static final float YAW_SENSITIVITY = 190f;
	private static final float MAX_PITCH = 90;
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.4f;
	private static final float FAR_PLANE = 2500;
	
	private static final float Y_OFFSET = 2;
	
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f viewMatrix = new Matrix4f();
	private Matrix4f projectionViewMatrix = new Matrix4f();
	
	private Vector3f position = new Vector3f( 50, 0, 0 );
	
	private Mouse mouse;
	private Window window;
	
	private float yaw = 0;
	private SmoothFloat pitch = new SmoothFloat( 10, 10 );
	private SmoothFloat angleAroundPlayer = new SmoothFloat( 0, 10 );
	private SmoothFloat distanceFromPlayer = new SmoothFloat( 10, 5 );
	
	public Camera( Mouse mouse, Window window ) {
		this.mouse = mouse;
		this.window = window;
		calculateProjectionMatrix();
		window.addSizeChangeListener( ( width, height ) -> 
			calculateProjectionMatrix() );
	}
	
	public void move() {
		calculatePitch();
		calculateAngleAroundPlayer();
		calculateZoom();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition( horizontalDistance, verticalDistance );
		this.yaw = 360 - angleAroundPlayer.get();
		yaw %= 360;
		updateViewMatrix();
	}
	

	@Override
	public Matrix4f getViewMatrix() { return viewMatrix; }
	@Override
	public Matrix4f getProjectionMatrix() { return projectionMatrix; }
	@Override
	public Matrix4f getProjectionViewMatrix() { return Matrix4f.mul( projectionMatrix, viewMatrix, projectionViewMatrix); }
	
	Vector3f negativeCameraPos = new Vector3f();
	public void updateViewMatrix() {
		viewMatrix.setIdentity();
		Matrix4f.rotate( ( float ) Math.toRadians( pitch.get() ),  new Vector3f( 1, 0, 0 ), viewMatrix, viewMatrix );
		Matrix4f.rotate( ( float ) Math.toRadians( yaw ), new Vector3f( 0, 1, 0 ), viewMatrix, viewMatrix );
		negativeCameraPos.set( -position.x, -position.y, -position.z );
		Matrix4f.translate( negativeCameraPos, viewMatrix, viewMatrix );
	}
	
	public void calculateProjectionMatrix() {
		this.projectionMatrix.setIdentity();
		float aspectRatio = window.getAspectRatio();
		float y_scale = ( float ) ( ( 1f / Math.tan( Math.toRadians( FOV / 2f ) ) ) );
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = - ( ( FAR_PLANE + NEAR_PLANE ) / frustum_length );
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = - ( ( 2 * NEAR_PLANE * FAR_PLANE ) / frustum_length );
		projectionMatrix.m33 = 0;
	}
	
	private void calculateCameraPosition( float horizDistance, float verticDistance ) {
		double theta = Math.toRadians( angleAroundPlayer.get() );
		position.x = 50 + ( float ) ( horizDistance * Math.sin( theta ) );
		position.y = verticDistance + Y_OFFSET;
		position.z = 50 + ( float ) ( horizDistance * Math.cos( theta ) );
		//TODO: ThinMatrix does the toRad calc inside the sin/cos call ( declares theta as float angleAroundPlayer ) ???
	}
	
	/** @return The horizontal distance of the camera from the origin. */
	private float calculateHorizontalDistance() {
		return ( float ) ( distanceFromPlayer.get() * Math.cos( Math.toRadians( pitch.get() ) ) );
	}
	
	/** @return The height of the camera from the aim point. */
	private float calculateVerticalDistance() {
		return ( float ) ( distanceFromPlayer.get() * Math.sin( Math.toRadians( pitch.get() ) ) );
	}
	
	/**
	 * Calculate the pitch; change if the user is dragging the mouse with RMB down.
	 */
	private void calculatePitch() {
		if ( mouse.isButtonDown( MouseButton.RIGHT ) ) {
			float pitchChange = mouse.getDy() * PITCH_SENSITIVITY;
			pitch.increaseTarget( pitchChange );
			clampPitch();
		}
		pitch.update( Orrea.instance.getDeltaSeconds() );
	}
	
	private void calculateZoom() {
		float targetZoom = distanceFromPlayer.getTarget();
		float zoomLevel = mouse.getScroll() * 0.08f * targetZoom;
		targetZoom -= zoomLevel;
		if( targetZoom < 1 )
			targetZoom = 1;
		distanceFromPlayer.setTarget( targetZoom );
		distanceFromPlayer.update( Orrea.instance.getDeltaSeconds() );
	}
	
	/**
	 * Calculate the angle of the camera around the player ( when looking down at
	 * the camera from above ); i.e. the yaw. Changes the yaw when the user drags
	 * the mouse with RMB down.
	 */
	private void calculateAngleAroundPlayer() {
		if( mouse.isButtonDown( MouseButton.RIGHT ) ) {
			float angleChange = mouse.getDx() * YAW_SENSITIVITY;
			angleAroundPlayer.increaseTarget( -angleChange );
		}
		angleAroundPlayer.update( Orrea.instance.getDeltaSeconds() );
	}
	
	/** Ensures the camera's pitch isn't too high/low */
	private void clampPitch() {
		if( pitch.getTarget() < 0 )
			pitch.setTarget( 0 );
		else if( pitch.getTarget() > MAX_PITCH )
			pitch.setTarget( MAX_PITCH );
	}
}
