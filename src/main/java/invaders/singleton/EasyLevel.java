package invaders.singleton;

public class EasyLevel {

    private static EasyLevel instance;
    private String configPath;

    private EasyLevel() {
        configPath = "src/main/resources/config_easy.json";
    }

    public static EasyLevel getInstance() {
        if(instance == null) {
            instance = new EasyLevel();
        }

        return EasyLevel.instance;
    }

    public String getConfigPath() {
        return configPath;
    }

}
