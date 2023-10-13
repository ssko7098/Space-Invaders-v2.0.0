package invaders.factory;

import invaders.engine.GameEngine;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.strategy.ProjectileStrategy;
import javafx.scene.image.Image;

public class EnemyProjectile extends Projectile{
    private ProjectileStrategy strategy;
    private boolean counted = false;

    public EnemyProjectile(Vector2D position, ProjectileStrategy strategy, Image image) {
        super(position,image);
        this.strategy = strategy;
    }

    @Override
    public void update(GameEngine model) {
        strategy.update(this);

        if(this.getPosition().getY()>= model.getGameHeight() - this.getImage().getHeight()){
            this.takeDamage(1);
        }
        else if(!isAlive() && !counted) {
            int points = 0;
            if(strategy.getStrategy().equals("slow_straight")) {
                points = 1;
            }
            else if(strategy.getStrategy().equals("fast_straight")) {
                points = 2;
            }

            model.getScore().addScore(points);
            counted = true;
        }

    }
    @Override
    public String getRenderableObjectName() {
        return "EnemyProjectile";
    }

    @Override
    public String getStrategy() {
        return strategy.toString();
    }
}
