package net.artelnatif.nicko.disguise;

import java.util.Locale;

public class NickoProfile implements Cloneable {
    public static final NickoProfile EMPTY_PROFILE = new NickoProfile(null, null, Locale.ENGLISH);

    private String name;
    private String skin;
    private Locale locale;

    public NickoProfile(String name, String skin, Locale locale) {
        this.name = name;
        this.skin = skin;
        this.locale = locale;
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

    public Locale getLocale() { return locale; }

    public void setLocale(Locale locale) { this.locale = locale; }

    @Override
    public String toString() {
        return "NickoProfile{" +
                "name='" + name + '\'' +
                ", skin='" + skin + '\'' +
                ", empty='" + isEmpty() + '\'' +
                '}';
    }

    @Override
    public NickoProfile clone() {
        Object o;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return (NickoProfile) o;
    }
}
