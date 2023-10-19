package invaders.singleton;

public class HardLevel {

    private static HardLevel instance;
    private String configPath;

    private HardLevel() {
        configPath = "src/main/resources/config_hard.json";
    }

    public static HardLevel getInstance() {
        if(instance == null) {
            instance = new HardLevel();
        }

        return HardLevel.instance;
    }

    public String getConfigPath() {
        return configPath;
    }

}
