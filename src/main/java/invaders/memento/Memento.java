package invaders.memento;

import invaders.engine.GameEngine;
import invaders.entities.Player;
import invaders.factory.*;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.observer.Score;
import invaders.observer.Timer;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;

import java.util.ArrayList;
import java.util.List;

public class Memento {

    private Score score;
    private Timer timer;
    private ArrayList<Enemy> enemies;
    private ArrayList<Renderable> existingEnemies;
    private ArrayList<Projectile> enemyProjectiles;
    private ArrayList<Renderable> existingProjectiles;
    private ArrayList<Projectile> playerProjectiles;
    private ArrayList<Bunker> bunkers;
    private Player player;

    public Memento(GameEngine engine) {
        this.score = new Score(engine.getScore());
        this.timer = new Timer(engine.getTimer());
        this.enemies = new ArrayList<>();
        this.enemyProjectiles = new ArrayList<>();
        this.existingEnemies = new ArrayList<>();
        this.existingProjectiles = new ArrayList<>();
        this.playerProjectiles = new ArrayList<>();
        this.player = new Player(engine.getPlayer());
        this.bunkers = new ArrayList<>();

        for(int i=0; i<engine.getRenderables().size(); i++) {
            Renderable renderable = engine.getRenderables().get(i);

            if(renderable.getRenderableObjectName().equals("Enemy")) {
                Enemy enemy = (Enemy) renderable;
                Enemy newEnemy = new Enemy(enemy);

                enemies.add(newEnemy);
                existingEnemies.add(renderable);
            }

            else if(renderable.getRenderableObjectName().equals("EnemyProjectile")) {
                EnemyProjectile oldP = (EnemyProjectile) renderable;
                Projectile newP = new EnemyProjectile(oldP);

                enemyProjectiles.add(newP);
                existingProjectiles.add(renderable);
            }

            else if(renderable.getRenderableObjectName().equals("PlayerProjectile")) {
                PlayerProjectile oldP = (PlayerProjectile) renderable;
                Projectile newP = new PlayerProjectile(oldP);

                playerProjectiles.add(newP);
                existingProjectiles.add(renderable);
            }

            else if(renderable.getRenderableObjectName().equals("Bunker")) {
                Bunker oldB = (Bunker) renderable;
                Bunker newB = new Bunker(oldB);

                bunkers.add(newB);
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

    public List<Renderable> getExistingEnemies() {
        return existingEnemies;
    }

    public List<Projectile> getEnemyProjectiles() {
        return enemyProjectiles;
    }

    public List<Renderable> getExistingProjectiles() {
        return existingProjectiles;
    }

    public List<Projectile> getPlayerProjectiles() {
        return playerProjectiles;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Bunker> getBunkers() {
        return bunkers;
    }

}
