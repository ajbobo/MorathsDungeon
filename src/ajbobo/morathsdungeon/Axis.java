package ajbobo.morathsdungeon;

import javax.microedition.khronos.opengles.GL10;

public class Axis extends Drawable3D
{
	public Axis()
	{
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
		
		float vertices[] = {
				0,0,0, // Origin = red
				1,0,0, // X = green
				0,1,0, // Y = blue
				0,0,1  // Z  purple
		};
		
		short indices[] = {
				0, 1,
				0, 2,
				0, 3
		};
		
		fillColorBuffer(colors);
		fillVertexBuffer(vertices);
		fillIndexBuffer(indices);
	}
	
	public void draw(GL10 gl)
	{
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexbuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorbuffer);
		gl.glDrawElements(GL10.GL_LINES, vertexcnt, GL10.GL_UNSIGNED_SHORT, indexbuffer);
	}
}
