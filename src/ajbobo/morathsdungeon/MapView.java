package ajbobo.morathsdungeon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

public class MapView extends Activity
{
	private MapViewer viewer;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		
		Maze maze = intent.getParcelableExtra("Maze");
		
		viewer = new MapViewer(this, maze);
		setContentView(viewer);
	}
	
	private class MapViewer extends View
	{
		private Maze _maze;
		private Paint paint;

		public MapViewer(Context context, Maze inmaze)
		{
			super(context);
			
			_maze = inmaze;
			
			paint = new Paint();
		}
		
		public void onDraw(Canvas canvas)
		{
			int border = 5;
			
			int canvaswidth = canvas.getWidth() - (2 * border);
			int canvasheight = canvas.getHeight() - (2 * border);
			
			int mazewidth = _maze.getWidth();
			int mazeheight = _maze.getHeight();
			
			int cellsize = Math.min(canvasheight / mazeheight, canvaswidth / mazewidth);
			
			int centerx = canvaswidth / 2 + border;
			int centery = canvasheight / 2 + border;
			
			int left = (int)(centerx - cellsize * ((float)mazewidth / 2));
			int top = (int)(centery - cellsize * ((float)mazeheight / 2));
			
			paint.setColor(Color.CYAN);
			canvas.drawRect(left - 2, top - 2, left + mazewidth * cellsize + 2, top + mazeheight * cellsize + 2, paint);
			
			for (int x = 0; x < mazewidth; x++)
			{
				for (int y = 0; y < mazeheight; y++)
				{
					MazeSpace curspace = _maze.getMazeSpace(x, y);
					if (curspace == null)
						continue;
					else if (curspace == Maze.WALL_SPACE)
						paint.setColor(Color.DKGRAY);
					else if (x == 1 && y == 1)
						paint.setColor(Color.BLUE);
					else
						paint.setColor(Color.LTGRAY);
					
					int spacex = left + (x * cellsize);
					int spacey = top + ((mazeheight - (y + 1)) * cellsize);
					canvas.drawRect(spacex, spacey, spacex + cellsize, spacey + cellsize, paint);
				}
			}
			
			paint.setColor(Color.BLUE);
			canvas.drawCircle(centerx, centery, 1, paint);
			
			
		}
		
	}

}
