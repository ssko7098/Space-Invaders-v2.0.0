package invaders.memento;

import invaders.ConfigReader;
import invaders.builder.Director;
import invaders.builder.EnemyBuilder;
import invaders.engine.GameEngine;
import invaders.factory.EnemyProjectileFactory;
import invaders.factory.Projectile;
import invaders.factory.ProjectileFactory;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.observer.Score;
import invaders.observer.Timer;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Memento {

    private Score score;
    private Timer timer;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    private ProjectileFactory pFactory = new EnemyProjectileFactory();

    public Memento(GameEngine engine) {
        this.score = new Score(engine.getScore());
        this.timer = new Timer(engine.getTimer());

        Director director = new Director();
        EnemyBuilder enemyBuilder = new EnemyBuilder();

        for(Renderable renderable : engine.getRenderables()) {
            if(renderable.getRenderableObjectName().equals("Enemy")) {
                Enemy enemy = (Enemy) renderable;
                Enemy newEnemy = new Enemy(renderable.getPosition());
                newEnemy.setImage(renderable.getImage());
                newEnemy.setProjectileStrategy(enemy.getProjectileStrategy());
                newEnemy.setProjectileImage(enemy.getProjectileImage());

                enemies.add(newEnemy);
            }
        }

//        for(Object eachEnemyInfo : ConfigReader.getEnemiesInfo()) {
//            Enemy enemy = director.constructEnemy(engine, enemyBuilder, (JSONObject) eachEnemyInfo);
//
//            for(Renderable renderable : engine.getRenderables()) {
//                if(renderable.getRenderableObjectName().equals("Enemy")) {
//                    enemy.setPosition(renderable.getPosition());
//                    Projectile p = pFactory.createProjectile(new Vector2D(enemy.getPosition().getX() + enemy.getImage().getWidth() / 2,
//                            enemy.getPosition().getY() + enemy.getImage().getHeight() + 2), enemy.getProjectileStrategy(), enemy.getProjectileImage());
//
//                    enemies.add(enemy);
//                    projectiles.add(p);
//                }
//            }
//        }
    }

    public Score getScore() {
        return score;
    }

    public Timer getTimer() {
        return timer;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

}
