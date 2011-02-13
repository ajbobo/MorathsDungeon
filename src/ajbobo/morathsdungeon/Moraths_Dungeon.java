package ajbobo.morathsdungeon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Moraths_Dungeon extends Activity
{
	private Maze _maze;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Button button = (Button) findViewById(R.id.btnNewGame);
		button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				StartGame();
			}
		});
		
		button = (Button) findViewById(R.id.btnLoadGame);
		button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				ShowToast("Coming Someday...");
			}
		});
		
		button = (Button) findViewById(R.id.btnOptions);
		button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				ShowToast("Coming Someday...");
			}
		});
	}
	
	public void StartGame()
	{
		_maze = new Maze(15,10);//30,30);
		//String temp = _maze.GetMazeString();
		Intent intent = new Intent();
		intent.setClass(this, MapView.class);
		intent.putExtra("Maze",_maze);
		startActivity(intent);
	}
	
	public void ShowToast(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}