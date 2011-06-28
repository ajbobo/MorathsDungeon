package ajbobo.morathsdungeon;

import javax.microedition.khronos.opengles.GL10;

import android.os.Parcel;
import android.os.Parcelable;

public class Player extends Character
{
	public Player()
	{
		super();
	}
	
	public Player(float X, float Y, float Rot)
	{
		super(X,Y,Rot);
	}
	
	public Player(Parcel parcel)
	{
		super(parcel);
	}
	
	public void draw(GL10 gl)
	{
		// Do not push/pop matrices here because the Player controls the view of everything else
		gl.glRotatef(getRot(), 0f, 1f, 0f);
		gl.glTranslatef(getX(), 0f, getY());
	}
	
	public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() // Requred by Parcelable
	{
		public Player createFromParcel(Parcel in)
		{
			return new Player(in);
		}

		public Player[] newArray(int size)
		{
			return new Player[size];
		}
	};
}
