package xyz.atnrch.nicko.i18n;

import java.util.ArrayList;
import java.util.Iterator;

public class ItemTranslator {
    private final String name;
    private final ArrayList<String> lore;

    public ItemTranslator(String name, ArrayList<String> lore) {
        this.name = name;
        this.lore = lore;
    }

    public Object[] translate(String... args) {
        // Add all elements to a list
        final ArrayList<String> toTranslate = new ArrayList<>();
        toTranslate.add(name);
        toTranslate.addAll(lore);

        // Set starting index to 0
        int index = 0;

        // While iterator next value exists/isn't null
        final Iterator<String> iterator = toTranslate.iterator();
        while (!iterator.hasNext() || iterator.next() == null) {
            // Get the current line
            final String currentLine = toTranslate.get(index);

            // Replace with the corresponding varargs index
            toTranslate.set(index, currentLine.replace("{" + index + "}", args[index]));

            // Increment the index
            index++;
        }
        return new Object[]{toTranslate.get(0), toTranslate.subList(1, toTranslate.size())};
    }
}
