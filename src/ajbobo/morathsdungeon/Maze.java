package ajbobo.morathsdungeon;

import java.util.Random;

import android.os.Parcel;
import android.os.Parcelable;

public class Maze implements Parcelable
{
	public static final MazeSpace WALL_SPACE = new MazeSpace();

	private MazeSpace[][] _maze;
	private Random rand = new Random();

	public Maze(int width, int height)
	{
		// Adjust the width to account for borders
		width += 2;
		height += 2;
		// Create the maze, add the borders
		_maze = new MazeSpace[width][height];
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
					_maze[x][y] = WALL_SPACE; // Start with a border of walls
				else
					_maze[x][y] = null; // null = a space that hasn't been assigned
			}
		}

		// Start filling in the maze
		TravelTo(1, 1);
	}
	
	public Maze(Parcel parcel)
	{
		int width = parcel.readInt();
		int height = parcel.readInt();
		
		_maze = new MazeSpace[width][height];
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				int ishall = parcel.readInt();
				if (ishall == 1)
				{
					_maze[x][y] = parcel.readParcelable(MazeSpace.class.getClassLoader());
				}
				else
				{
					_maze[x][y] = WALL_SPACE;
				}
			}
		}
		
	}

	private boolean AssignSpace(int x, int y)
	{
		MazeSpace curspace = _maze[x][y];
		if (curspace == WALL_SPACE)
			return false;
		else if (curspace != null) // The space is assigned a hall
			return true;

		if (rand.nextInt(4) == 0) // 25% chance of a wall
		{
			_maze[x][y] = WALL_SPACE;
			return false;
		}

		return true; // If we get here, then the space will be a hall
	}

	private void TravelTo(int x, int y)
	{
		// Make sure the space hasn't been visited
		if (_maze[x][y] != null)
			return;

		// Mark the space as a valid hall
		_maze[x][y] = new MazeSpace();

		// Decide which of the four orthagonal spaces are halls and which are walls
		boolean havenorth = AssignSpace(x, y + 1); // North
		boolean havesouth = AssignSpace(x, y - 1); // South
		boolean haveeast = AssignSpace(x + 1, y); // East
		boolean havewest = AssignSpace(x - 1, y); // West

		// Create diagonal walls appropriately
		if (havenorth && haveeast)
			_maze[x + 1][y + 1] = WALL_SPACE;
		if (havenorth && havewest)
			_maze[x - 1][y + 1] = WALL_SPACE;
		if (havesouth && haveeast)
			_maze[x + 1][y - 1] = WALL_SPACE;
		if (havesouth && havewest)
			_maze[x - 1][y - 1] = WALL_SPACE;

		// String temp = GetMazeString();

		// Travel to the orthagonal spaces and expand the maze from there
		TravelTo(x, y + 1); // North
		TravelTo(x, y - 1); // South
		TravelTo(x + 1, y); // East
		TravelTo(x - 1, y); // West
	}
	
	public int getHeight()
	{
		return _maze[0].length;
	}
	
	public int getWidth()
	{
		return _maze.length;
	}
	
	public MazeSpace getMazeSpace(int x, int y)
	{
		if (x < 0 || x >= _maze.length || y < 0 || y >= _maze[0].length)
			return null;
		
		return _maze[x][y];
	}
	
	public boolean isHallSpace(int x, int y)
	{
		MazeSpace curspace = getMazeSpace(x,y);
		
		if (curspace == null || curspace == WALL_SPACE)
			return false;
		
		return true;
	}

	public String getMazeString()
	{
		StringBuilder builder = new StringBuilder();

		for (int y = _maze[0].length - 1; y >= 0; y--)
		{
			for (int x = 0; x < _maze.length; x++)
			{
				if (_maze[x][y] == null)
					builder.append('-');
				else if (_maze[x][y] == WALL_SPACE)
					builder.append('H');
				else
					builder.append(' ');
			}
			builder.append('\n');
		}
		return builder.toString();
	}

	public int describeContents() // Requred by Parcelable
	{
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) // Requred by Parcelable
	{
		int width = _maze.length;
		int height = _maze[0].length;
		
		dest.writeInt(width);
		dest.writeInt(height);
		
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				MazeSpace curspace = _maze[x][y];
				if (curspace == WALL_SPACE)
				{
					dest.writeInt(0);
				}
				else
				{
					dest.writeInt(1);
					dest.writeParcelable(curspace, 0);
				}
			}
		}
	}

	public static final Parcelable.Creator<Maze> CREATOR = new Parcelable.Creator<Maze>() // Requred by Parcelable
	{
		public Maze createFromParcel(Parcel in)
		{
			return new Maze(in);
		}

		public Maze[] newArray(int size)
		{
			return new Maze[size];
		}
	};
}
