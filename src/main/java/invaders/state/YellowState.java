package invaders.state;
import invaders.gameobject.Bunker;
import javafx.scene.image.Image;
import java.io.File;

public class YellowState implements BunkerState {
    private Bunker bunker;

    public YellowState(Bunker bunker){
        this.bunker = bunker;
    }

    @Override
    public void takeDamage() {
        bunker.setImage(new Image(new File("src/main/resources/bunkerRed.png").toURI().toString()));
        bunker.setState(new RedState(bunker));
    }
}