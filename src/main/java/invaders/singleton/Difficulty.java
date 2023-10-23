package invaders.singleton;

public class Difficulty {

    private static Difficulty instance = null;
    private String configPath;

    private Difficulty() {}

    public static Difficulty getInstance() {
        if(instance == null) {
            instance = new Difficulty();

            // set default difficulty to easy
            instance.setDifficulty("easy");
        }

        return instance;
    }

    public void setDifficulty(String difficulty) {
        this.configPath = "src/main/resources/config_" + difficulty.toLowerCase() + ".json";
    }

    public String getConfigPath() {
        return configPath;
    }

}
