package ajbobo.morathsdungeon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

public class Maze extends Drawable3D implements Parcelable
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
		
		// Calculate vertices for the maze
		CalculateVertices();
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
		
		//Calculate vertices for the maze
		CalculateVertices();
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
	
	
	private void CalculateVertices()
	{
		int mazewidth = getWidth();
		int mazeheight = getHeight();
		float colors[] = { 
				1, 0, 0, 1, // red
				0, 1, 0, 1, // green
				0, 0, 1, 1, // blue
				1, 0, 1, 1, // purple
				1, 1, 0, 1, // yellow
				0, 1, 1, 1, // cyan
				1, 1, 1, 1, // white
				0, 0, 0, 1, // black 
			};
		
		fillColorBuffer(colors);

		// Create the array to hold the vertex information
		int valsperlevel = (mazewidth - 1) * (mazeheight - 1) * 3;
		vertexcnt = valsperlevel * 2;
		float vertices[] = new float[vertexcnt];
		int ptr = 0;
		for (int y = 0; y < mazeheight - 1; y++)
		{
			for (int x = 0; x < mazewidth - 1; x++)
			{
				int altptr = ptr + valsperlevel;
				vertices[ptr] = vertices[altptr] = -5 + (10 * x); // X and X'
				vertices[ptr + 1] = -5; // Y
				vertices[altptr + 1] = 5; // Y'
				vertices[ptr + 2] = vertices[altptr + 2] = 5 - (10 * y); // Z and Z'
	
				ptr += 3;
			}
		}
		fillVertexBuffer(vertices);
	
		// Create the array to hold the index information
		int m = mazewidth - 2;
		int n = mazeheight - 2;
		int maxvertices = 2 * ((2 * m * n) + m + n) * 3;
		ByteBuffer vbb = ByteBuffer.allocateDirect(maxvertices * 4);
		vbb.order(ByteOrder.nativeOrder());
		indexbuffer = vbb.asShortBuffer(); //IntBuffer.allocate(maxvertices * 4); // 4 = size of int
	
		ptr = 0;
		for (int y = 1; y <= n; y++)
		{
			for (int x = 1; x <= m; x++)
			{
				if (!isHallSpace(x, y))
					continue;
				
				short space = (short)((y - 1) * m + x);
				short a = (short)(space + (y - 1) - 1);
				short b = (short)(a + 1);
				short c = (short)(b + m);
				short d = (short)(c + 1);
	
				if (!isHallSpace(x, y + 1))
					addWall(c, d, valsperlevel / 3); // North
				if (!isHallSpace(x, y - 1))
					addWall(b, a, valsperlevel / 3); // South
				if (!isHallSpace(x + 1, y))
					addWall(d, b, valsperlevel / 3); // East
				if (!isHallSpace(x - 1, y))
					addWall(a, c, valsperlevel / 3); // West
	
			}
		}
		indexbuffer.position(0);
	}

	private void addWall(short index1, short index2, int offset)
	{
		try
		{
			indexbuffer.put(index1);
			indexbuffer.put(index2);
			indexbuffer.put((short)(index1 + offset));
	
			indexbuffer.put(index2);
			indexbuffer.put((short)(index2 + offset));
			indexbuffer.put((short)(index1 + offset));
		}
		catch (Exception ex)
		{
			//String temp = getMazeString();
			Toast.makeText(null, ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void draw(GL10 gl)
	{
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexbuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorbuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, vertexcnt, GL10.GL_UNSIGNED_SHORT, indexbuffer);
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
	
	public int getSpace(float loc)
	{
		return (int)Math.floor((loc + 5) / 10) + 1; // Magically, this works for X and Y
	}
		
	/*
	public boolean isHallPoint(float locX, float locY)
	{
		// Figure out which space in the array (locX,locY) is in
		int x = (int)Math.floor((locX + 5) / 10) + 1;
		int y = (int)Math.floor((locY + 5) / 10) + 1;
		
		// Return whether or not that array space is a hall
		return isHallSpace(x, y);
	}
	*/

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
