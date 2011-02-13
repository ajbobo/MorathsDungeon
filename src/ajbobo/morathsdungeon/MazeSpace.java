package ajbobo.morathsdungeon;

import android.os.Parcel;
import android.os.Parcelable;

public class MazeSpace implements Parcelable
{
	int[] textures;

	public MazeSpace()
	{
		textures = new int[4];
		for (int x = 0; x < 4; x++)
			textures[x] = 0;
	}
	
	public MazeSpace(Parcel parcel)
	{
		textures = new int[4];
		for (int x = 0; x < 4; x++)
			textures[x] = parcel.readInt();
	}

	public int describeContents() // Requred by Parcelable
	{
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) // Requred by Parcelable
	{
		for (int x = 0; x < 4; x++)
			dest.writeInt(textures[x]);
	}
	
	public static final Parcelable.Creator<MazeSpace> CREATOR = new Parcelable.Creator<MazeSpace>() // Requred by Parcelable
	{
		public MazeSpace createFromParcel(Parcel in)
		{
			return new MazeSpace(in);
		}

		public MazeSpace[] newArray(int size)
		{
			return new MazeSpace[size];
		}
	};
}
