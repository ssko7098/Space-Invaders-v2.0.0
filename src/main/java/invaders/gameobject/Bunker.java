package invaders.gameobject;

import invaders.engine.GameEngine;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import invaders.state.BunkerState;
import invaders.state.GreenState;
import javafx.scene.image.Image;

public class Bunker implements GameObject, Renderable {
    private Vector2D position;
    private double width;
    private double height;
    private int lives;
    private Image image;
    private BunkerState state = new GreenState(this);


    @Override
    public void start() {}

    @Override
    public void update(GameEngine model) {
        /*
        Logic TBD
         */

    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void takeDamage(double amount){
        this.lives -= 1;
        this.state.takeDamage();
    }

    @Override
	public double getHealth(){
	    return this.lives;
	}

    @Override
    public String getRenderableObjectName() {
        return "Bunker";
    }

    @Override
	public boolean isAlive(){
	    return this.lives > 0;
	}


    @Override
    public double getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public BunkerState getState() {
        return state;
    }

    public void setState(BunkerState state) {
        this.state = state;
    }
}
