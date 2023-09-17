package xyz.atnrch.nicko.i18n;

import java.util.ArrayList;

public class ItemTranslation {
    private final String name;
    private final ArrayList<String> lore;

    public ItemTranslation(String name, ArrayList<String> lore) {
        this.name = name;
        this.lore = lore;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getLore() {
        return lore;
    }
}
