package ajbobo.morathsdungeon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

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
import android.widget.Toast;

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
		private MazeRenderer renderer;
		private float lastX, lastY;

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

		private class MazeRenderer implements GLSurfaceView.Renderer
		{
			private Maze _maze;
			
			public float angle;

			private FloatBuffer vertexbuffer, colorbuffer;
			private ShortBuffer indexbuffer;
			private int vertexcnt;
			
			private float colors[] = { 
					1, 0, 0, 1, // red
					0, 1, 0, 1, // green
					0, 0, 1, 1, // blue
					1, 0, 1, 1, // purple
					1, 1, 0, 1, // yellow
					0, 1, 1, 1, // cyan
					1, 1, 1, 1, // white
					0, 0, 0, 1, // black
				};

			public MazeRenderer(Maze inmaze)
			{
				angle = 0;
				_maze = inmaze;
				int mazewidth = _maze.getWidth();
				int mazeheight = _maze.getHeight();

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
						vertices[ptr] = vertices[altptr] = 1 * x; // X and X'
						vertices[ptr + 1] = 0; // Y
						vertices[altptr + 1] = 1; // Y'
						vertices[ptr + 2] = vertices[altptr + 2] = -1 * y; // Z and Z'

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
			}

			private void addWall(short index1, short index2, int offset)
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
			}

			public void onDrawFrame(GL10 gl)
			{
				gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

				gl.glMatrixMode(GL10.GL_MODELVIEW);
				gl.glLoadIdentity();

				// Position the camera
				gl.glTranslatef(-.5f, -.5f, -1f);
				gl.glRotatef(angle, 0, 1, 0);
				
				gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

				// Draw the Maze
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexbuffer);
				gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorbuffer);
				gl.glDrawElements(GL10.GL_TRIANGLES, vertexcnt, GL10.GL_UNSIGNED_SHORT, indexbuffer);
			}

			public void onSurfaceChanged(GL10 gl, int width, int height)
			{
				// Set the viewport to the entire screen (or at least the part allocated for the program)
				gl.glViewport(0, 0, width, height);

				// Set the frustum so that squares are actually square on the screen
				float ratio = (float) width / height;
				gl.glMatrixMode(GL10.GL_PROJECTION);
				gl.glLoadIdentity();
				gl.glFrustumf(-ratio, ratio, -1, 1, .5f, 2.1f);
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
