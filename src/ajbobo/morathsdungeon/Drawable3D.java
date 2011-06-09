package ajbobo.morathsdungeon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Drawable3D
{
	protected FloatBuffer vertexbuffer, colorbuffer;
	protected ShortBuffer indexbuffer;
	protected int vertexcnt;
	
	public Drawable3D()
	{
		vertexcnt = 0;
	}
	
	protected void fillVertexBuffer(float[] arr)
	{
		vertexbuffer = makeFloatBuffer(arr);
	}
	
	protected void fillColorBuffer(float[] arr)
	{
		colorbuffer = makeFloatBuffer(arr);
	}
	
	protected void fillIndexBuffer(short[] arr)
	{
		indexbuffer = makeShortBuffer(arr);
		vertexcnt = arr.length;
	}
	
	public void draw(GL10 gl)
	{
		// Does nothing here - subclasses draw themselves
	}
	
	public static FloatBuffer makeFloatBuffer(float[] arr)
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4); // floats are 4 bytes
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}

	public static ShortBuffer makeShortBuffer(short[] arr)
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 2); // shorts are 2 bytes
		bb.order(ByteOrder.nativeOrder());
		ShortBuffer ib = bb.asShortBuffer();
		ib.put(arr);
		ib.position(0);
		return ib;
	}
}
