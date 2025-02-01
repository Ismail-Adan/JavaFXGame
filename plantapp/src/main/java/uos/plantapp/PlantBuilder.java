package uos.plantapp;

import javafx.scene.canvas.GraphicsContext;

public class PlantBuilder {
	private GraphicsContext gc;
    private double x;
    private double y;
    private Subject subject;

    public PlantBuilder(GraphicsContext gc) {
        this.gc = gc;
    }

    public PlantBuilder setX(double x) {
        this.x = x;
        return this;
    }

    public PlantBuilder setY(double y) {
        this.y = y;
        return this;
    }

    public PlantBuilder setSubject(Subject subject) {
        this.subject = subject;
        return this;
    }

    public Plant build() {
        Plant plant = new Plant(gc, x, y);
        if (subject != null) {
            subject.addObserver(plant);
        }
        return plant;
    }

}
