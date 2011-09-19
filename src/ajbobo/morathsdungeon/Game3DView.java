package ajbobo.morathsdungeon;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
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
	private GameRules _rules; // This will be accessible to all the internal classes, too

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		_rules = intent.getParcelableExtra("rules");

		viewer = new GLViewer(this);

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
			intent.putExtra("Maze", _rules.getMaze());
			intent.putExtra("Player", _rules.getPlayer());
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
		private GameRenderer renderer;
		private float lastX, lastY;

		public GLViewer(Context context)
		{
			super(context);

			renderer = new GameRenderer(context, this);
			setRenderer(renderer);
			setRenderMode(RENDERMODE_WHEN_DIRTY);
		}

		@Override 
		public boolean onTouchEvent(MotionEvent e) 
		{
			float y = e.getY();
			float x = e.getX();
			float width = this.getWidth();
			float height = this.getHeight();
			
			switch(e.getAction())
			{
			case MotionEvent.ACTION_MOVE:
				if (y > height / 2) // Ignore movement in the top half of the screen
				{
					float diff = lastX - x;
					_rules.rotatePlayer((diff / width) * 180);
				
					diff = lastY - y;
					_rules.movePlayer((diff / height) * 120);
				}
				
				requestRender();
				break;
			}

			lastX = x;
			lastY = y;
			
			return true;
		}

		private class GameRenderer implements GLSurfaceView.Renderer
		{
			private GLViewer _view;
			
			//public Axis _axis;

			public GameRenderer(Context context, GLViewer view)
			{
				_view = view;
				
				//_axis = new Axis();
			}
			
			public void onDrawFrame(GL10 gl)
			{
				gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

				gl.glPushMatrix();

				// Camera's initial position
				gl.glRotatef(180, 0, 1, 0);
				
				//_axis.draw(gl);
				_rules.draw(gl);
				
				gl.glPopMatrix();
			}

			public void onSurfaceChanged(GL10 gl, int width, int height)
			{
				// Set the viewport to the entire screen (or at least the part allocated for the program)
				gl.glViewport(0, 0, width, height);
			}

			public void onSurfaceCreated(GL10 gl, EGLConfig config)
			{
				// Set the frustum so that squares are actually square on the screen
				gl.glMatrixMode(GL10.GL_PROJECTION);
				gl.glLoadIdentity();
				
				// the 90 degree field of view makes the Eye of Beholder mazey look
				GLU.gluPerspective(gl, 90.0f, (_view.getWidth() * 1f) / _view.getHeight(), 1, 100);
				
				// After fighting with this command for a long time, I found these parameters that match up well
				// with my AC3D workspace, and then I just control the view using 
				// the scale, translate, and rotate in the draw code
				GLU.gluLookAt(gl, 0f, 0f, -1f, 0f, 0f, 0f, 0f, 1.0f, 0f);
				
				// By default, OpenGL enables features that improve quality but reduce performance. One might want to tweak that especially on software renderer.
				gl.glDisable(GL10.GL_DITHER);
				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

				// Some one-time OpenGL initialization can be made here probably based on features of this particular context
				//gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
				gl.glClearColor(0, 0, .25f, 1);
				gl.glEnable(GL10.GL_CULL_FACE);
				gl.glShadeModel(GL10.GL_SMOOTH);
				gl.glEnable(GL10.GL_DEPTH_TEST);
			}
		}
	}
}
