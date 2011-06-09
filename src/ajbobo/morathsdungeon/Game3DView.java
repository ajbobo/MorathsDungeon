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
			intent.putExtra("Maze", _maze);
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

		public GLViewer(Context context, Maze inmaze)
		{
			super(context);

			renderer = new GameRenderer(inmaze, context, this);
			setRenderer(renderer);
			setRenderMode(RENDERMODE_WHEN_DIRTY);
		}

		@Override 
		public boolean onTouchEvent(MotionEvent e) 
		{
			float y = e.getY();
			float x = e.getX();
			
			switch(e.getAction())
			{
			case MotionEvent.ACTION_MOVE:
				float diff = lastX - x;
				float width = this.getWidth();
				renderer.angle += (diff / width) * 180;
				
				requestRender();
				break;
			}

			lastX = x;
			lastY = y;
			
			return true;
		}

		private class GameRenderer implements GLSurfaceView.Renderer
		{
			private Context _context;
			private GLViewer _view;
			
			public float angle;
			
			private Maze _maze;
			public Axis _axis;

		
			public GameRenderer(Maze inmaze, Context context, GLViewer view)
			{
				_context = context;
				_view = view;
				angle = 0;
				
				_maze = inmaze;
				_axis = new Axis();
				
				
/* NOTE: Make the Maze class a Drawable3D object and move this stuff into it
				// Create the array to hold the vertex information
				int valsperlevel = (mazewidth - 1) * (mazeheight - 1) * 3;
				vertexcnt = valsperlevel * 2;
				float vertices[] = new float[vertexcnt];

				ByteBuffer vbb = ByteBuffer.allocateDirect(colors.length * 4);
				vbb.order(ByteOrder.nativeOrder());
				colorbuffer = vbb.asFloatBuffer();
				colorbuffer.put(colors);
				colorbuffer.position(0);
				
				int ptr = 0;
				for (int y = 0; y < mazeheight - 1; y++)
				{
					for (int x = 0; x < mazewidth - 1; x++)
					{
						int altptr = ptr + valsperlevel;
						vertices[ptr] = vertices[altptr] = 5 * x - 5; // X and X'
						vertices[ptr + 1] = -5; // Y
						vertices[altptr + 1] = 5; // Y'
						vertices[ptr + 2] = vertices[altptr + 2] = -5 * y - 5; // Z and Z'

						ptr += 3;
					}
				}
				vbb = ByteBuffer.allocateDirect(vertices.length * 4); // 4 = size of float
				vbb.order(ByteOrder.nativeOrder());
				vertexbuffer = vbb.asFloatBuffer();
				vertexbuffer.put(vertices);
				vertexbuffer.position(0);

				// Create the array to hold the index information
				int m = mazewidth - 2;
				int n = mazeheight - 2;
				int maxvertices = 2 * ((2 * m * n) + m + n) * 3;
				vbb = ByteBuffer.allocateDirect(maxvertices * 4);
				vbb.order(ByteOrder.nativeOrder());
				indexbuffer = vbb.asShortBuffer(); //IntBuffer.allocate(maxvertices * 4); // 4 = size of int
				// indexbuffer.allocate(maxvertices); // This could be too big - it will be compacted later

				ptr = 0;
				for (int y = 1; y <= n; y++)
				{
					for (int x = 1; x <= m; x++)
					{
						if (!_maze.isHallSpace(x, y))
							continue;
						
						short space = (short)((y - 1) * m + x);
						short a = (short)(space + (y - 1) - 1);
						short b = (short)(a + 1);
						short c = (short)(b + m);
						short d = (short)(c + 1);

						if (!_maze.isHallSpace(x, y + 1))
							addWall(c, d, valsperlevel / 3); // North
						if (!_maze.isHallSpace(x, y - 1))
							addWall(b, a, valsperlevel / 3); // South
						if (!_maze.isHallSpace(x + 1, y))
							addWall(d, b, valsperlevel / 3); // East
						if (!_maze.isHallSpace(x - 1, y))
							addWall(a, c, valsperlevel / 3); // West

					}
				}
				indexbuffer.position(0);
				colorbuffer.position(0);
				*/
			}

			/*private void addWall(short index1, short index2, int offset)
			{
				int cnt;
				try
				{
					indexbuffer.put(index1);
					indexbuffer.put(index2);
					indexbuffer.put((short)(index1 + offset));

					indexbuffer.put(index2);
					indexbuffer.put((short)(index2 + offset));
					indexbuffer.put((short)(index1 + offset));
				}
				catch (Exception ex)
				{
					String temp = _maze.getMazeString();
					Toast.makeText(null, ex.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}*/
			
			public void onDrawFrame(GL10 gl)
			{
				gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

				gl.glPushMatrix();

				// Position the camera
				gl.glRotatef(angle, 0, 1, 0);
				
				_axis.draw(gl);
				
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
				GLU.gluLookAt(gl, 0f, 2f, -4f, 0.0f, 0.0f, 0f, 0.0f, 1.0f, 1.0f);
				
				// By default, OpenGL enables features that improve quality but reduce performance. One might want to tweak that especially on software renderer.
				gl.glDisable(GL10.GL_DITHER);
				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

				// Some one-time OpenGL initialization can be made here probably based on features of this particular context
				//gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
				gl.glClearColor(0, 0, .25f, 1);
				//gl.glEnable(GL10.GL_CULL_FACE);
				gl.glShadeModel(GL10.GL_SMOOTH);
				gl.glEnable(GL10.GL_DEPTH_TEST);
			}
		}
	}
}
