package uos.plantapp;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class GameObject {
	protected Image img;
	protected double x, y;
	protected GraphicsContext gc;
	
	public GameObject(GraphicsContext gc, double x, double y)
	{
		super();
		this.gc=gc;
		this.x=227;
		this.y=25;
	}
	
	public void update()
	{
		if(img!=null)
		gc.drawImage(img, x, y, 250, 385);
	}

}
