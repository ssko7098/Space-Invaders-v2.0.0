package invaders.gameobject;

import invaders.engine.GameEngine;
import invaders.factory.EnemyProjectile;
import invaders.factory.EnemyProjectileFactory;
import invaders.factory.Projectile;
import invaders.factory.ProjectileFactory;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import invaders.strategy.ProjectileStrategy;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy implements GameObject, Renderable {
    private Vector2D position;
    private int lives = 1;
    private Image image;
    private int xVel = -1;

    private ArrayList<Projectile> enemyProjectile;
    private ArrayList<Projectile> pendingToDeleteEnemyProjectile;
    private ProjectileStrategy projectileStrategy;
    private ProjectileFactory projectileFactory;
    private Image projectileImage;
    private Random random = new Random();
    private boolean counted = false;

    public Enemy(Vector2D position) {
        this.position = position;
        this.projectileFactory = new EnemyProjectileFactory();
        this.enemyProjectile = new ArrayList<>();
        this.pendingToDeleteEnemyProjectile = new ArrayList<>();
    }

    public Enemy(Enemy enemy) {
        this.position = new Vector2D(enemy.position.getX(), enemy.position.getY());
        this.projectileFactory = enemy.projectileFactory;
        this.enemyProjectile = enemy.enemyProjectile;
        this.pendingToDeleteEnemyProjectile = enemy.pendingToDeleteEnemyProjectile;
        this.image = enemy.image;
        this.projectileStrategy = enemy.projectileStrategy;
        this.projectileImage = enemy.projectileImage;
        this.xVel = enemy.xVel;
        this.lives = enemy.lives;
    }

    @Override
    public void start() {}

    @Override
    public void update(GameEngine engine) {
        if(enemyProjectile.size()<3){
            if(this.isAlive() &&  random.nextInt(120)==20){
                Projectile p = projectileFactory.createProjectile(new Vector2D(position.getX() + this.image.getWidth() / 2, position.getY() + image.getHeight() + 2),projectileStrategy, projectileImage);
                enemyProjectile.add(p);
                engine.getPendingToAddGameObject().add(p);
                engine.getPendingToAddRenderable().add(p);
            }
        }else{
            pendingToDeleteEnemyProjectile.clear();
            for(Projectile p : enemyProjectile){
                if(!p.isAlive()){
                    engine.getPendingToRemoveGameObject().add(p);
                    engine.getPendingToRemoveRenderable().add(p);
                    pendingToDeleteEnemyProjectile.add(p);
                }
            }

            for(Projectile p: pendingToDeleteEnemyProjectile){
                enemyProjectile.remove(p);
            }
        }

        if(this.position.getX()<=this.image.getWidth() || this.position.getX()>=(engine.getGameWidth()-this.image.getWidth()-1)){
            this.position.setY(this.position.getY()+25);
            xVel*=-1;
        }

        this.position.setX(this.position.getX() + xVel);

        if((this.position.getY()+this.image.getHeight())>=engine.getPlayer().getPosition().getY()){
            engine.getPlayer().takeDamage(Integer.MAX_VALUE);
        }

        for(Renderable renderable : engine.getRenderables()) {
            if(!isAlive() && !counted && this.isColliding(renderable) && renderable.getRenderableObjectName().equals("PlayerProjectile")) {
                int points = 0;
                if(projectileStrategy.getStrategy().equals("slow_straight")) {
                    points = 3;
                }
                else if(projectileStrategy.getStrategy().equals("fast_straight")) {
                    points = 4;
                }
                engine.getScore().addScore(points);
                counted = true;
            }
        }

    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return this.image.getWidth();
    }

    @Override
    public double getHeight() {
       return this.image.getHeight();
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setProjectileImage(Image projectileImage) {
        this.projectileImage = projectileImage;
    }

    @Override
    public void takeDamage(double amount) {
        this.lives-=1;
    }

    @Override
    public double getHealth() {
        return this.lives;
    }

    @Override
    public String getRenderableObjectName() {
        return "Enemy";
    }

    @Override
    public boolean isAlive() {
        return this.lives>0;
    }

    public void setProjectileStrategy(ProjectileStrategy projectileStrategy) {
        this.projectileStrategy = projectileStrategy;
    }

    public ProjectileStrategy getProjectileStrategy() {
        return projectileStrategy;
    }

    public List<Projectile> getEnemyProjectiles() {
        return enemyProjectile;
    }

    public Image getProjectileImage() {
        return projectileImage;
    }

    public void setSpeed(int xVel) {
        this.xVel = xVel;
    }

    public int getSpeed() {
        return xVel;
    }

}
