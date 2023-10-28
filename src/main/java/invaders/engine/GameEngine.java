package invaders.engine;

import java.util.ArrayList;
import java.util.List;

import invaders.ConfigReader;
import invaders.builder.BunkerBuilder;
import invaders.builder.Director;
import invaders.builder.EnemyBuilder;
import invaders.entities.EntityView;
import invaders.factory.EnemyProjectile;
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.entities.Player;
import invaders.memento.Caretaker;
import invaders.memento.Memento;
import invaders.observer.Observer;
import invaders.observer.Score;
import invaders.observer.Subject;
import invaders.observer.Timer;
import invaders.rendering.Renderable;
import invaders.singleton.Difficulty;
import org.json.simple.JSONObject;

/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine {
	private List<GameObject> gameObjects = new ArrayList<>(); // A list of game objects that gets updated each frame
	private List<GameObject> pendingToAddGameObject = new ArrayList<>();
	private List<GameObject> pendingToRemoveGameObject = new ArrayList<>();

	private List<Renderable> pendingToAddRenderable = new ArrayList<>();
	private List<Renderable> pendingToRemoveRenderable = new ArrayList<>();

	private List<Renderable> renderables =  new ArrayList<>();

	private Player player;

	private boolean left;
	private boolean right;
	private int gameWidth;
	private int gameHeight;
	private int counter = 45;

	private Score score = new Score();
	private Timer timer = new Timer();

	public GameEngine(String config){
		// Read the config here
		ConfigReader.parse(config);

		// Get game width and height
		gameWidth = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("x")).intValue();
		gameHeight = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("y")).intValue();

		//Get player info
		this.player = new Player(ConfigReader.getPlayerInfo());
		renderables.add(player);

		Director director = new Director();
		BunkerBuilder bunkerBuilder = new BunkerBuilder();
		//Get Bunkers info
		for(Object eachBunkerInfo:ConfigReader.getBunkersInfo()){
			Bunker bunker = director.constructBunker(bunkerBuilder, (JSONObject) eachBunkerInfo);
			gameObjects.add(bunker);
			renderables.add(bunker);
		}


		EnemyBuilder enemyBuilder = new EnemyBuilder();
		//Get Enemy info
		for(Object eachEnemyInfo:ConfigReader.getEnemiesInfo()){
			Enemy enemy = director.constructEnemy(this,enemyBuilder,(JSONObject)eachEnemyInfo);
			gameObjects.add(enemy);
			renderables.add(enemy);
		}

		timer.setTimer(new double[]{0, 0});

	}

	/**
	 * Updates the game/simulation
	 */
	public void update(){
		counter+=1;

		timer.setTimer(new double[]{timer.getTimer()[0], timer.getTimer()[1] + 0.0085});

		movePlayer();

		for(GameObject go: gameObjects){
			go.update(this);
		}

		for (int i = 0; i < renderables.size(); i++) {
			Renderable renderableA = renderables.get(i);
			for (int j = i+1; j < renderables.size(); j++) {
				Renderable renderableB = renderables.get(j);

				if((renderableA.getRenderableObjectName().equals("Enemy") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))
						||(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("Enemy"))||
						(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))){
				}else{
					if(renderableA.isColliding(renderableB) && (renderableA.getHealth()>0 && renderableB.getHealth()>0)) {
						renderableA.takeDamage(1);
						renderableB.takeDamage(1);
					}
				}
			}
		}


		// ensure that renderable foreground objects don't go off-screen
		int offset = 1;
		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}
			if(ro.getPosition().getX() + ro.getWidth() >= gameWidth) {
				ro.getPosition().setX((gameWidth - offset) -ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(offset);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= gameHeight) {
				ro.getPosition().setY((gameHeight - offset) -ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(offset);
			}
		}

	}

	public List<Renderable> getRenderables(){
		return renderables;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
	public List<GameObject> getPendingToAddGameObject() {
		return pendingToAddGameObject;
	}

	public List<GameObject> getPendingToRemoveGameObject() {
		return pendingToRemoveGameObject;
	}

	public List<Renderable> getPendingToAddRenderable() {
		return pendingToAddRenderable;
	}

	public List<Renderable> getPendingToRemoveRenderable() {
		return pendingToRemoveRenderable;
	}


	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public boolean shootPressed(){
		if(counter>45 && player.isAlive()){
			Projectile projectile = player.shoot();
			gameObjects.add(projectile);
			renderables.add(projectile);
			counter=0;
			return true;
		}
		return false;
	}

	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}

	public int getGameWidth() {
		return gameWidth;
	}

	public int getGameHeight() {
		return gameHeight;
	}

	public Player getPlayer() {
		return player;
	}

	public Timer getTimer() {return timer;}

	public Score getScore() {return score;}

	public Memento saveState() {
		return new Memento(this);
	}

	public void recoverState(Memento m) {
		this.timer.setTimer(m.getTimer().getTimer());
		this.score.setScore(m.getScore().getScore());

		//Restore the state of the player
		player.getPosition().setX(m.getPlayer().getPosition().getX());
		player.getPosition().setY(m.getPlayer().getPosition().getY());
		player.setHealth((int) m.getPlayer().getHealth());

		pendingToAddRenderable.addAll(m.getRenderables());
		pendingToAddGameObject.addAll(m.getGameObjects());
		pendingToRemoveGameObject.addAll(gameObjects);

		//Reset all entities apart from the player
		for(Renderable entity : renderables) {
			if(!entity.getRenderableObjectName().equals("Player") && !entity.getRenderableObjectName().equals("Bunker")) {
				entity.takeDamage(Integer.MAX_VALUE);
			}

			//special reset for bunker because it only loses 1 health no matter what
			else if(entity.getRenderableObjectName().equals("Bunker")) {
				entity.takeDamage(1);
				entity.takeDamage(1);
				entity.takeDamage(1);
			}
		}
	}

	public void enemyCheat() {
		for(Renderable renderable : renderables) {
			if(renderable.getRenderableObjectName().equals("Enemy") && renderable.isAlive()) {
				if(renderable.getImage().getUrl().endsWith("fast_alien.png")){
					renderable.takeDamage(Integer.MAX_VALUE);
					score.addScore(4);
				}
			}
		}
	}

	public void projectileCheat() {
		for(Renderable renderable : renderables) {
			if(renderable.getRenderableObjectName().equals("EnemyProjectile") && renderable.isAlive()) {
				if(renderable.getImage().getUrl().endsWith("alien_shot_fast.png")){
					renderable.takeDamage(Integer.MAX_VALUE);
					score.addScore(2);
				}
			}
		}
	}

	public void endGame() {
		for(Renderable renderable : renderables) {
			if(renderable.getRenderableObjectName().equals("Bunker")) {
				renderable.takeDamage(1);
				renderable.takeDamage(1);
				renderable.takeDamage(1);
			}
			else {
				renderable.takeDamage(Integer.MAX_VALUE);
			}
		}
	}

	public void resetGame(String config) {
		endGame();

		// Read the config here
		ConfigReader.parse(config);

		// Get game width and height
		gameWidth = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("x")).intValue();
		gameHeight = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("y")).intValue();

		//Get player info
		this.player = new Player(ConfigReader.getPlayerInfo());
		renderables.add(player);

		Director director = new Director();
		BunkerBuilder bunkerBuilder = new BunkerBuilder();
		//Get Bunkers info
		for(Object eachBunkerInfo:ConfigReader.getBunkersInfo()){
			Bunker bunker = director.constructBunker(bunkerBuilder, (JSONObject) eachBunkerInfo);
			gameObjects.add(bunker);
			renderables.add(bunker);
		}


		EnemyBuilder enemyBuilder = new EnemyBuilder();
		//Get Enemy info
		for(Object eachEnemyInfo:ConfigReader.getEnemiesInfo()){
			Enemy enemy = director.constructEnemy(this,enemyBuilder,(JSONObject)eachEnemyInfo);
			gameObjects.add(enemy);
			renderables.add(enemy);
		}

		timer.setTimer(new double[]{0, 0});
		score.setScore(0);
		counter = 45;
	}

}
