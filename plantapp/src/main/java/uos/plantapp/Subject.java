package uos.plantapp;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private List<PlantObserverIF> observers = new ArrayList<>();
    private int waterLevel;
    public int getWaterLevel() {
		return waterLevel;
	}

	public int getSunlightLevel() {
		return sunlightLevel;
	}

	private int sunlightLevel;
	private boolean isWithered;

    public void addObserver(PlantObserverIF observer) {
        observers.add(observer);
    }

    public void removeObserver(PlantObserverIF observer) {
        observers.remove(observer);
    }

    public void setWaterLevel(int waterLevel) {
        // Restrict water level to be between 0 and 100
        this.waterLevel = Math.min(Math.max(waterLevel, 0), 100);
        notifyObservers();
    }


    public void setSunlightLevel(int sunlightLevel) {
    	// Restrict sun light level to be between 0 and 100
        this.sunlightLevel = Math.min(Math.max(sunlightLevel, 0), 100);
        notifyObservers();
    }
    
    public void setWithered(boolean isWithered) {
    	this.isWithered = isWithered;
        notifyObservers();
    }   

    private void notifyObservers() {
        for (PlantObserverIF observer : observers) {
            observer.updateFields(waterLevel, sunlightLevel, isWithered);
        }
    }
}

