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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Memento {

    private Score score;
    private Timer timer;
    private Player player;

    private ArrayList<Renderable> renderables;
    private ArrayList<GameObject> gameObjects;

    public Memento(GameEngine engine) {
        this.score = new Score(engine.getScore());
        this.timer = new Timer(engine.getTimer());
        this.renderables = new ArrayList<>();
        this.gameObjects = new ArrayList<>();

        this.player = new Player(engine.getPlayer());

        for(int i=0; i<engine.getRenderables().size(); i++) {
            Renderable renderable = engine.getRenderables().get(i);

            if(renderable.getRenderableObjectName().equals("Enemy")) {
                Enemy enemy = (Enemy) renderable;
                Enemy newEnemy = new Enemy(enemy);

                renderables.add(newEnemy);
                gameObjects.add(newEnemy);
            }

            else if(renderable.getRenderableObjectName().equals("EnemyProjectile")) {
                EnemyProjectile oldP = (EnemyProjectile) renderable;
                Projectile newP = new EnemyProjectile(oldP);

                renderables.add(newP);
                gameObjects.add(newP);
            }

            else if(renderable.getRenderableObjectName().equals("PlayerProjectile")) {
                PlayerProjectile oldP = (PlayerProjectile) renderable;
                Projectile newP = new PlayerProjectile(oldP);

                renderables.add(newP);
                gameObjects.add(newP);
            }

            else if(renderable.getRenderableObjectName().equals("Bunker")) {
                Bunker oldB = (Bunker) renderable;
                Bunker newB = new Bunker(oldB);

                renderables.add(newB);
            }
        }
    }

    public Score getScore() {
        return score;
    }

    public Timer getTimer() {
        return timer;
    }

    public List<Renderable> getRenderables() { return renderables; }

    public List<GameObject> getGameObjects() {return gameObjects;}

    public Player getPlayer() {return player;}

}
