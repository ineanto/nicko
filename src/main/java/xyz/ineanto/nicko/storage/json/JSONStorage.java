package xyz.ineanto.nicko.storage.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import xyz.ineanto.nicko.Nicko;
import xyz.ineanto.nicko.appearance.ActionResult;
import xyz.ineanto.nicko.profile.NickoProfile;
import xyz.ineanto.nicko.storage.Storage;
import xyz.ineanto.nicko.storage.StorageProvider;

import java.io.*;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class JSONStorage extends Storage {
    private final Logger logger = Logger.getLogger("JSONStorage");
    private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private final File directory = new File(Nicko.getInstance().getDataFolder() + "/players/");

    private JSONStorageProvider provider;

    @Override
    public StorageProvider getProvider() {
        if (provider == null) {
            provider = new JSONStorageProvider(directory);
        }
        return provider;
    }

    @Override
    public ActionResult store(UUID uuid, NickoProfile profile) {
        final String profileToJson = gson.toJson(profile);
        final File file = new File(directory, uuid.toString() + ".json");

        try {
            if (checkFileExists(file)) {
                try (FileWriter fileWriter = new FileWriter(file)) {
                    try (BufferedWriter writer = new BufferedWriter(fileWriter)) {
                        writer.write(profileToJson);
                    }
                } catch (IOException e) {
                    logger.warning("Could not write to file.");
                    return ActionResult.error();
                }
            }
        } catch (IOException e) {
            logger.warning("Could not create file.");
            e.printStackTrace();
            return ActionResult.error();
        }

        return ActionResult.ok();
    }

    @Override
    public boolean isStored(UUID uuid) {
        final File file = new File(directory, uuid.toString() + ".json");
        return file.exists();
    }

    @Override
    public Optional<NickoProfile> retrieve(UUID uuid) {
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

    @Override
    public ActionResult delete(UUID uuid) {
        final File file = new File(directory, uuid.toString() + ".json");
        if (file.delete() || !file.exists()) {
            return ActionResult.ok();
        }
        return ActionResult.error();
    }

    private boolean checkFileExists(File file) throws IOException {
        // Additional check if the folder gets deleted while the plugin is running.
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            return file.createNewFile();
        }
        return true;
    }
}
