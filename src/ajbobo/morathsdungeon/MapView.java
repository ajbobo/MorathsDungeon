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
		
		viewer = new MapViewer(this);
		setContentView(viewer);
	}
	
	private class MapViewer extends View
	{
		//private Maze _maze[][];
		private Paint paint;

		public MapViewer(Context context)
		{
			super(context);
			
			paint = new Paint();
			paint.setColor(Color.LTGRAY);
		}
		
		public void onDraw(Canvas canvas)
		{
			canvas.drawRect(10, 10, 50, 200, paint);
		}
		
	}

}
