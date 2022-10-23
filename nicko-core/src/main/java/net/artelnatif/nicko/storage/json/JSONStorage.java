package net.artelnatif.nicko.storage.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.artelnatif.nicko.NickoBukkit;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.storage.Storage;
import net.artelnatif.nicko.storage.StorageProvider;

import java.io.*;
import java.util.Optional;
import java.util.UUID;

public class JSONStorage extends Storage {
    final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    final File directory = new File(NickoBukkit.getInstance().getDataFolder() + "/players/");

    @Override
    public StorageProvider getProvider() { return new JSONStorageProvider(directory); }

    @Override
    public void store(UUID uuid, NickoProfile profile) {
        final String profileToJson = gson.toJson(profile);
        final File file = new File(directory, uuid.toString() + ".json");

        try {
            if (checkFileExists(file)) {
                try (FileWriter fileWriter = new FileWriter(file)) {
                    try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
                        writer.write(profileToJson);
                    }
                } catch (IOException e) {
                    System.out.println("Could not write to file.");
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not create file.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isStored(UUID uuid) {
        final File directory = new File(NickoBukkit.getInstance().getDataFolder() + "/players/");
        final File file = new File(directory, uuid.toString() + ".json");
        return file.exists();
    }

    @Override
    public Optional<NickoProfile> retrieve(UUID uuid) {
        final File directory = new File(NickoBukkit.getInstance().getDataFolder() + "/players/");
        final File file = new File(directory, uuid.toString() + ".json");
        try (FileReader fileReader = new FileReader(file)) {
            try (BufferedReader reader = new BufferedReader(fileReader)) {
                NickoProfile value = gson.fromJson(reader, NickoProfile.class);
                return Optional.of(value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkFileExists(File file) throws IOException {
        if (!file.exists()) {
            return file.createNewFile();
        }
        return true;
    }
}
