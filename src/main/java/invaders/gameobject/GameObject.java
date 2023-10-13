package invaders.gameobject;

import invaders.engine.GameEngine;

// contains basic methods that all GameObjects must implement
public interface GameObject {

    public void start();
    public void update(GameEngine model);

}
