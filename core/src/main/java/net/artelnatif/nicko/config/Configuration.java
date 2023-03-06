package net.artelnatif.nicko.config;

public class Configuration {
    private final String address;
    private final String username;
    private final String password;
    private final String prefix;
    private final Boolean local;
    private final Boolean customLocale;

    public Configuration(String address, String username, String password, String prefix, Boolean local, Boolean customLocale) {
        this.address = address;
        this.username = username;
        this.password = password;
        this.prefix = prefix;
        this.local = local;
        this.customLocale = customLocale;
    }

    public Configuration() {
        this("", "", "", "", false, false);
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPrefix() {
        return prefix;
    }

    public Boolean isLocal() {
        return local;
    }

    public Boolean isCustomLocale() {
        return customLocale;
    }
}
