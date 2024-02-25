package xyz.ineanto.nicko.appearance.random;

import xyz.ineanto.nicko.NickoBukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomNameFetcher {
    private final NickoBukkit instance;

    public RandomNameFetcher(NickoBukkit instance) {
        this.instance = instance;
    }

    public String getRandomUsername() {
        final InputStream resource = instance.getResource("names.txt");
        final List<List<String>> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] values = line.split("\n");
                records.add(Arrays.asList(values));
            }
            return records.get(new Random().nextInt(records.size() - 1)).get(0);
        } catch (IOException e) {
            instance.getLogger().severe("Unable to fetch random names.");
            return "Ineanto";
        }
    }
}
