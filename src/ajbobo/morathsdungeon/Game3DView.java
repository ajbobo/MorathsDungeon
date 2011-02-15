package ajbobo.morathsdungeon;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class Game3DView extends Activity
{
	private static final int MENU_SHOW_MAP = Menu.FIRST;
	
	private GLViewer viewer;
	private Maze _maze;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		_maze = intent.getParcelableExtra("Maze");
		
		viewer = new GLViewer(this, _maze);

		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
		setContentView(viewer);
		viewer.requestFocus();
		viewer.setFocusableInTouchMode(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(0, MENU_SHOW_MAP, 0, "Show Map");
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case MENU_SHOW_MAP:
		{
			Intent intent = new Intent();
			intent.setClass(this, MapView.class);
			intent.putExtra("Maze",_maze);
			startActivity(intent);
			return true;
		}
		}
		
		return false;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		viewer.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		viewer.onPause();
	}
	
	private class GLViewer extends GLSurfaceView
	{
		private MazeRenderer renderer;
		
		public GLViewer(Context context, Maze inmaze)
		{
			super(context);
			
			renderer = new MazeRenderer(inmaze);
			setRenderer(renderer);
			setRenderMode(RENDERMODE_WHEN_DIRTY);
		}
		
		@Override 
		public boolean onTouchEvent(MotionEvent e) 
		{
			// FINISH ME
			
			return false;
		}
		
		private class MazeRenderer implements GLSurfaceView.Renderer
		{
			private Maze _maze;
			
			private FloatBuffer vertexbuffer, colorbuffer;
			private ByteBuffer indexbuffer;
			
			public MazeRenderer(Maze inmaze)
			{
				_maze = inmaze;
				
				// Fill the buffers based on the information in the Maze
				int mazewidth = _maze.getWidth();
				int mazeheight = _maze.getHeight();
				for (int x = 0; x < mazewidth; x++)
				{
					for (int y = 0; y < mazeheight; y++)
					{
						
					}
				}
			}

			public void onDrawFrame(GL10 gl)
			{
				gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();
				
				// Position the camera
				//gl.glTranslatef(0, 0, -distance);
				//gl.glRotatef(-angle, 0, 1, 0);
				
				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

				// Draw the Maze
				//gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexbuffer);
				//gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorbuffer);
				//gl.glDrawElements(primitive, indices.length, GL10.GL_UNSIGNED_BYTE, indexbuffer);
			}

			public void onSurfaceChanged(GL10 gl, int width, int height)
			{
				// Set the viewport to the entire screen (or at least the part allocated for the program)
				gl.glViewport(0, 0, width, height);

				// Set the frustum so that squares are actually square on the screen
				float ratio = (float) width / height;
				gl.glMatrixMode(GL10.GL_PROJECTION);
				gl.glLoadIdentity();
				gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
			}

			public void onSurfaceCreated(GL10 gl, EGLConfig config)
			{
				// By default, OpenGL enables features that improve quality but reduce performance. One might want to tweak that especially on software renderer.
				gl.glDisable(GL10.GL_DITHER);

				// Some one-time OpenGL initialization can be made here probably based on features of this particular context
				gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
				gl.glClearColor(0, 0, .25f, 1);
				gl.glEnable(GL10.GL_CULL_FACE);
				gl.glShadeModel(GL10.GL_SMOOTH);
				gl.glEnable(GL10.GL_DEPTH_TEST);
			}
		}
	}
}
