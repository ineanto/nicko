package com.yoshiplex.rainbow;

import java.util.Arrays;
import java.util.List;

public class RainbowText {
    private static final List<String> RAINBOW = Arrays.asList("§4", "§c", "§6", "§a", "§2", "§b", "§3", "§9", "§1", "§5", "§d");
    private int place;
    private List<String> rainbowArray = null;
    // STOPSHIP: 7/28/22 i18n
    private String text = "You did not provide any text.";
    private String fancyText = "§4You did not provide any text";
    private String prefix = "";

    public RainbowText(String text) {
        this(text, (String) null);
    }

    public RainbowText(String text, String formatCode) {
        place = 0;
        if (text != null) {
            this.text = text;
        }
        if (formatCode != null) {
            prefix = formatCode;
        }
        rainbowArray = RAINBOW;
        updateFancy();
    }

    private void updateFancy() {
        int spot = place;
        String fancyText = "";
        for (char l : text.toCharArray()) {
            String letter = Character.toString(l);
            String t1 = fancyText;
            if (!letter.equalsIgnoreCase(" ")) {
                fancyText = t1 + rainbowArray.get(spot) + prefix + letter;
                if (spot == rainbowArray.size() - 1) {
                    spot = 0;
                } else {
                    spot++;
                }
            } else {
                fancyText = t1 + letter;
            }
        }
        this.fancyText = fancyText;
    }

    public void moveRainbow() {
        if (rainbowArray.size() - 1 == place) {
            place = 0;
        } else {
            place++;
        }
        updateFancy();
    }

    public void moveRainbowRight() {
        if (place == 0) {
            place = rainbowArray.size() - 1;
        } else {
            place--;
        }
        updateFancy();
    }

    public String getOrigonalText() {
        return this.text;
    }

    public String getText() {
        return this.fancyText;
    }

    public void setPlace(int place) {
        if (place > RAINBOW.size() - 1 || place < 0) {
            return;
        }
        this.place = place;
        updateFancy();
    }

    public int getPlace() {
        return this.place;
    }

    public List<String> getRainbow() {
        return rainbowArray;
    }

    public String getFormatPrefix() {
        return this.prefix;
    }

    public void setFormatPrefix(String prefix) {
        this.prefix = prefix;
    }

    public static List<String> getDefaultRainbow() {
        return RAINBOW;
    }
}
