package xyz.atnrch.nicko.profile;

public class AppearanceData {
    private String name;
    private String skin;

    public AppearanceData(String name, String skin) {
        this.name = name;
        this.skin = skin;
    }

    public boolean isEmpty() {
        return name == null && skin == null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }
}
