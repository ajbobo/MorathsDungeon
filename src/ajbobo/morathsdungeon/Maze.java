package ajbobo.morathsdungeon;

import java.util.Random;

public class Maze
{
	private final MazeSpace WALL_SPACE = new MazeSpace();

	private MazeSpace[][] _maze;

	private Random rand = new Random();

	public Maze(int width, int height)
	{
		_maze = new MazeSpace[width][height];
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
					_maze[x][y] = WALL_SPACE; // Start with a border of walls-
				else
					_maze[x][y] = null; // null = a space that hasn't been assigned
			}
		}

		TravelTo(1, 1);
	}
	
	private MazeSpace AssignSpace(int x, int y)
	{
		if (_maze[x][y] != null)
			return _maze[x][y]; // Don't reassign the space
	
		
		if (rand.nextInt(4) == 0) // 25% chance of a wall
			_maze[x][y] = WALL_SPACE;

		return _maze[x][y]; // Returning a null means that the space is valid, but not yet traveled
	}
	
	private void TravelTo(int x, int y)
	{
		// Make sure the space hasn't been visited
		if (_maze[x][y] != null)
			return;
		
		// Mark the space as a valid hall
		_maze[x][y] = new MazeSpace();
		
		// Decide which of the four orthagonal spaces are halls and which are walls
		boolean havenorth = (AssignSpace(x,y + 1) != WALL_SPACE); // North
		boolean havesouth = (AssignSpace(x,y - 1) != WALL_SPACE); // South
		boolean haveeast = (AssignSpace(x + 1,y) != WALL_SPACE); // East
		boolean havewest = (AssignSpace(x - 1,y) != WALL_SPACE); // West
		
		// Create diagonal walls appropriately
		if (havenorth && haveeast) _maze[x + 1][y + 1] = WALL_SPACE;
		if (havenorth && havewest) _maze[x - 1][y + 1] = WALL_SPACE;
		if (havesouth && haveeast) _maze[x + 1][y - 1] = WALL_SPACE;
		if (havesouth && havewest) _maze[x - 1][y - 1] = WALL_SPACE;

		
		String temp = GetMazeString();
		
		// Travel to the orthagonal spaces and expand the maze from there
		TravelTo(x,y + 1); // North
		TravelTo(x,y - 1); // South
		TravelTo(x + 1,y); // East
		TravelTo(x - 1,y); // West
	}

	public String GetMazeString()
	{
		StringBuilder builder = new StringBuilder();

		for (int y = _maze[0].length - 1; y >= 0 ; y--)
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

	private class MazeSpace
	{
		int[] textures;

		public MazeSpace()
		{
			textures = new int[4];
			for (int x = 0; x < 4; x++)
				textures[x] = 0;
		}
	}
}
