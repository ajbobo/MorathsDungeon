package ajbobo.morathsdungeon;

import javax.microedition.khronos.opengles.GL10;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Character implements Parcelable
{
	private float _x;
	private float _y;
	private float _rot;
	
	abstract public void draw(GL10 gl);
	
	public Character()
	{
		setLoc(0,0);
		setRot(0);
	}
	
	public Character(float startX, float startY, float startRot)
	{
		setLoc(startX,startY);
		setRot(startRot);
	}
	
	public Character(Parcel parcel)
	{
		_x = parcel.readFloat();
		_y = parcel.readFloat();
		_rot = parcel.readFloat();
	}

	public float getX()
	{
		return _x;
	}
	
	public float getY()
	{
		return _y;
	}
	
	public float getRot()
	{
		return _rot;
	}
	
	public void setLoc(float X, float Y)
	{
		_x = X;
		_y = Y;
	}
	
	public void moveLoc(float X, float Y)
	{
		_x += X;
		_y += Y;
	}
	
	public void setRot(float Rot)
	{
		_rot = Rot;
	}
	
	public void incrementRot(float Rot)
	{
		_rot += Rot;
	}
	
	public int describeContents() // Requred by Parcelable
	{
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) // Requred by Parcelable
	{
		dest.writeFloat(_x);
		dest.writeFloat(_y);
		dest.writeFloat(_rot);
	}
}
