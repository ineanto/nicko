package net.artelnatif.nicko.storage.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.artelnatif.nicko.Nicko;
import net.artelnatif.nicko.bukkit.NickoBukkit;
import net.artelnatif.nicko.disguise.ActionResult;
import net.artelnatif.nicko.disguise.NickoProfile;
import net.artelnatif.nicko.bukkit.i18n.I18NDict;
import net.artelnatif.nicko.storage.Storage;
import net.artelnatif.nicko.storage.StorageProvider;

import java.io.*;
import java.util.Optional;
import java.util.UUID;

public class JSONStorage extends Storage {
    private final Nicko nicko;
    private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private final File directory = new File(NickoBukkit.getInstance().getDataFolder() + "/players/");

    private JSONStorageProvider provider;

    public JSONStorage(Nicko nicko) { this.nicko = nicko; }

    @Override
    public StorageProvider getProvider() {
        if (provider == null) {
            provider = new JSONStorageProvider(directory);
        }
        return provider;
    }

    @Override
    public ActionResult<Void> store(UUID uuid, NickoProfile profile) {
        final String profileToJson = gson.toJson(profile);
        final File file = new File(directory, uuid.toString() + ".json");

        try {
            if (checkFileExists(file)) {
                try (FileWriter fileWriter = new FileWriter(file)) {
                    try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
                        writer.write(profileToJson);
                    }
                } catch (IOException e) {
                    nicko.getLogger().warning("Could not write to file.");
                    return new ActionResult<>(I18NDict.Error.JSON_ERROR);
                }
            }
        } catch (IOException e) {
            nicko.getLogger().warning("Could not create file.");
            return new ActionResult<>(I18NDict.Error.JSON_ERROR);
        }

        return new ActionResult<>();
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
                final NickoProfile value = gson.fromJson(reader, NickoProfile.class);
                return Optional.of(value);
            }
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private boolean checkFileExists(File file) throws IOException {
        if (!file.exists()) {
            return file.createNewFile();
        }
        return true;
    }
}
