package spotlight.spotlight.utils;

public enum LightTypes {
    SPOTLIGHT("spotlight", "Spotlight"),
    CRAFTING("crafting", "Crafting"),
    IS_ACTIVE("spotlightActive", "Active"),
    TRAILS("trail", "Trails"),
    COOLDOWN("cooldown", "CoolDown"),
    DOWN_LIGHT("downlight", "Downlight");

    private final String key;
    private final String label;

    private LightTypes(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public String getKey() {
        return this.key;
    }

    public String getLabel() {
        return this.label;
    }
}
