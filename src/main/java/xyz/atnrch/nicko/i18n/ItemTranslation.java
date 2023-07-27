package xyz.atnrch.nicko.i18n;

import java.util.List;

public class ItemTranslation {
    private final String name;
    private final List<String> lore;

    public ItemTranslation(String name, List<String> lore) {
        this.name = name;
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }
}
