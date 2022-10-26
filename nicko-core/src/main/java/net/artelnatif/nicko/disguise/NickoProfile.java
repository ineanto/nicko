package net.artelnatif.nicko.disguise;

public class NickoProfile {
    public static final NickoProfile EMPTY_PROFILE = new NickoProfile(null, null);

    private String name;
    private String skin;

    public NickoProfile(String name, String skin) {
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

    @Override
    public String toString() {
        return "NickoProfile{" +
                "name='" + name + '\'' +
                ", skin='" + skin + '\'' +
                ", empty='" + isEmpty() + '\'' +
                '}';
    }
}
