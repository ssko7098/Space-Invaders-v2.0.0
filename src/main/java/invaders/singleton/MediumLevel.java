package invaders.singleton;

public class MediumLevel {

    private static MediumLevel instance;
    private String configPath;

    private MediumLevel() {
        configPath = "src/main/resources/config_medium.json";
    }

    public static MediumLevel getInstance() {
        if(instance == null) {
            instance = new MediumLevel();
        }

        return MediumLevel.instance;
    }

    public String getConfigPath() {
        return configPath;
    }

}
