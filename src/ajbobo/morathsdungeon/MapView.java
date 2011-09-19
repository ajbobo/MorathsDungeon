package ajbobo.morathsdungeon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MapView extends Activity
{
	private MapViewer viewer;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		
		Maze maze = intent.getParcelableExtra("Maze");
		Player player = intent.getParcelableExtra("Player");
		
		viewer = new MapViewer(this, maze, player);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
       
		setContentView(viewer);
	}
	
	private class MapViewer extends View
	{
		private Maze _maze;
		private Player _player;
		private Paint paint;

		public MapViewer(Context context, Maze inmaze, Player inplayer)
		{
			super(context);
			
			_maze = inmaze;
			_player = inplayer;
			
			paint = new Paint();
		}
		
		public void onDraw(Canvas canvas)
		{
			// Calculate the size and location of the maze on the screen
			int border = 0; // This is here in case I decide to use it again in the future
			
			int canvaswidth = canvas.getWidth() - (2 * border);
			int canvasheight = canvas.getHeight() - (2 * border);
			
			int mazewidth = _maze.getWidth();
			int mazeheight = _maze.getHeight();
			
			int cellsize = Math.min(canvasheight / mazeheight, canvaswidth / mazewidth);
			
			int centerx = canvaswidth / 2 + border;
			int centery = canvasheight / 2 + border;
			
			int left = (int)(centerx - cellsize * ((float)mazewidth / 2));
			int top = (int)(centery - cellsize * ((float)mazeheight / 2));
			
			// Color the maze
			for (int x = 0; x < mazewidth; x++)
			{
				for (int y = 0; y < mazeheight; y++)
				{
					MazeSpace curspace = _maze.getMazeSpace(x, y);
					if (curspace == null)
						continue;
					else if (curspace == Maze.WALL_SPACE) // Don't draw walls
						continue;
					else if (x == 1 && y == 1)
						paint.setColor(Color.BLUE);
					else
						paint.setColor(Color.LTGRAY);
					
					int spacex = left + (x * cellsize);
					int spacey = top + ((mazeheight - (y + 1)) * cellsize);
					canvas.drawRect(spacex, spacey, spacex + cellsize, spacey + cellsize, paint);
				}
			}
		}
		
	}

}
