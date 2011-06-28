package ajbobo.morathsdungeon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Moraths_Dungeon extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
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
		GameRules rules = new GameRules();

		Intent intent = new Intent();
		intent.setClass(this, Game3DView.class);
		intent.putExtra("rules",rules);
		startActivity(intent);
	}
	
	public void ShowToast(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}