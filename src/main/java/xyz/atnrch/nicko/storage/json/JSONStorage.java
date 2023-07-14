package xyz.atnrch.nicko.storage.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import xyz.atnrch.nicko.NickoBukkit;
import xyz.atnrch.nicko.appearance.ActionResult;
import xyz.atnrch.nicko.profile.NickoProfile;
import xyz.atnrch.nicko.i18n.I18NDict;
import xyz.atnrch.nicko.storage.Storage;
import xyz.atnrch.nicko.storage.StorageProvider;

import java.io.*;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class JSONStorage extends Storage {
    private final Logger logger = Logger.getLogger("JSONStorage");
    private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private final File directory = new File(NickoBukkit.getInstance().getDataFolder() + "/players/");

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
                    return ActionResult.error(I18NDict.Error.JSON_ERROR);
                }
            }
        } catch (IOException e) {
            logger.warning("Could not create file.");
            return ActionResult.error(I18NDict.Error.JSON_ERROR);
        }

        return ActionResult.ok();
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
