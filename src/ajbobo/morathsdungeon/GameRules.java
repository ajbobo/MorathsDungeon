package ajbobo.morathsdungeon;

import javax.microedition.khronos.opengles.GL10;

import android.os.Parcel;
import android.os.Parcelable;

public class GameRules implements Parcelable
{
	private Maze _maze;
	private Player _player;
	
	public GameRules()
	{
		_maze = new Maze(10,10); // Do not include the outer walls in the dimensions
		_player = new Player(0,0,0);
	}
	
	public GameRules(Parcel parcel)
	{
		_maze = parcel.readParcelable(Maze.class.getClassLoader());
		_player = parcel.readParcelable(Player.class.getClassLoader());
	}
	
	public Maze getMaze()
	{
		return _maze;
	}
	
	public Player getPlayer()
	{
		return _player;
	}
	
	public int describeContents() // Requred by Parcelable
	{
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) // Requred by Parcelable
	{
		dest.writeParcelable(_maze, 0);
		dest.writeParcelable(_player, 0);
	}
	
	public static final Parcelable.Creator<GameRules> CREATOR = new Parcelable.Creator<GameRules>() // Requred by Parcelable
	{
		public GameRules createFromParcel(Parcel in)
		{
			return new GameRules(in);
		}

		public GameRules[] newArray(int size)
		{
			return new GameRules[size];
		}
	};

	public void rotatePlayer(float angle)
	{
		_player.incrementRot(angle);
		
	}

	public void movePlayer(float amount)
	{
		float rot = _player.getRot();
		float a = (float) (amount * Math.sin(Math.toRadians(rot)));
		float b = (float) (amount * Math.cos(Math.toRadians(rot)));
		_player.moveLoc(-a, -b);
	}

	public void draw(GL10 gl)
	{
		_player.draw(gl);
		_maze.draw(gl);
		
	}
}
