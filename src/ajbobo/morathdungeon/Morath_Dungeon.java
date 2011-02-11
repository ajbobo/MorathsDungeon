package ajbobo.morathdungeon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Morath_Dungeon extends Activity
{
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
				ShowToast("Coming Soon...");
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
	
	public void ShowToast(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}