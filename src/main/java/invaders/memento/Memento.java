package invaders.memento;

import invaders.engine.GameEngine;
import invaders.factory.EnemyProjectileFactory;
import invaders.factory.Projectile;
import invaders.factory.ProjectileFactory;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.observer.Score;
import invaders.observer.Timer;
import invaders.rendering.Renderable;

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

        for(int i=0; i<engine.getRenderables().size(); i++) {
            Renderable renderable = engine.getRenderables().get(i);

            if(renderable.getRenderableObjectName().equals("Enemy")) {
                Enemy enemy = (Enemy) renderable;
                Enemy newEnemy = new Enemy(enemy);

                enemies.add(newEnemy);
            }
        }
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
