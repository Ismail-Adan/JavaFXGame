package uos.plantapp;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Plant extends GameObject implements PlantObserverIF{
	private int waterLevel;
	private int sunLightLevel;
	private boolean isWithered;
	private boolean messageDisplayed;
	



	public Plant(GraphicsContext gc, double x, double y) {
		super(gc, x, y);
		this.waterLevel = 100;
		this.sunLightLevel = 100;
		this.isWithered = false;
		messageDisplayed = false;
		img = new Image(Plant.class.getResource("healthyPlant.png").toExternalForm());
		update();
	}
	
	public void update() {
		super.update();
	    if (waterLevel <= 0 || sunLightLevel <= 0) {
	        gc.clearRect(x, y, img.getWidth(), img.getHeight()); // Clear the area occupied by the old image
	        img = new Image(Plant.class.getResource("witheredPlant2.png").toExternalForm());
	        gc.drawImage(img, x, y); // Draw the withered plant image
	        // Show failure message
	        if(isWithered && !messageDisplayed) {
	        System.out.println("Your plant has withered. You failed to sustain it.");
	        setWithered(false);
	        setMessageDisplayed(true);
	        }
	    }
	    else {
	    	gc.clearRect(x, y, img.getWidth(), img.getHeight()); // Clear the area occupied by the old image (this is when plant.update() is called when the game is restarted
	        img = new Image(Plant.class.getResource("healthyPlant.png").toExternalForm());
	        gc.drawImage(img, x, y); // Draw the withered plant image
	    	
	    }
    }
	
	public void waterPlant() {
		waterLevel += 10;
		//update();
	}
	
	public void provideSunLight() {
		sunLightLevel += 10;
		//update();
	}
	


	
	

	public int getWaterLevel() {
		return waterLevel;
	}

	public void setWaterLevel(int waterLevel) {
		this.waterLevel = waterLevel;
	}

	public int getSunLightLevel() {
		return sunLightLevel;
	}

	public void setSunLightLevel(int sunLightLevel) {
		this.sunLightLevel = sunLightLevel;
	}

	public boolean isWithered() {
		return isWithered;
	}

	public void setWithered(boolean isWithered) {
		this.isWithered = isWithered;
	}
	
	public boolean isMessageDisplayed() {
		return messageDisplayed;
	}

	public void setMessageDisplayed(boolean messageDisplayed) {
		this.messageDisplayed = messageDisplayed;
	}
	
	@Override
	/*
	 * An abstract method in the Observer interface, plant which is an observer updates its fields whenever Subject (the maintainer)
	 * notifies it to update the fields
	 */
	public void updateFields(int waterLevel, int sunLightLevel, boolean isWithered) {
		setWaterLevel(waterLevel);
		setSunLightLevel(sunLightLevel);
		setWithered(isWithered);
	}
	

	
	

}
