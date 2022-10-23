package net.artelnatif.nicko.test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.artelnatif.nicko.mojang.MojangAPI;
import net.artelnatif.nicko.mojang.MojangSkin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class MojangAPITest {
    public static final String NAME = "Notch";
    public static final String URL_NAME = "https://api.mojang.com/users/profiles/minecraft/{name}";
    public static final String URL_SKIN = "https://sessionserver.mojang.com/session/minecraft/profile/{uuid}?unsigned=false";

    public static JsonObject object = null;

    @Test
    @DisplayName("MojangAPI - GET - Name")
    public void testGetMojangAPIName() throws IOException {
        final URL url = new URL(URL_NAME.replace("{name}", NAME));
        final HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);

        //DataOutputStream output = new DataOutputStream(urlConnection.getOutputStream());
        //output.writeBytes();
        //output.flush();
        //output.close();

        final BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        final StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        object = JsonParser.parseString(response.toString()).getAsJsonObject();
        Assertions.assertEquals(NAME, object.get("name").getAsString());
    }

    @Test
    @DisplayName("MojangAPI - GET - Skin")
    public void testGetMojangAPISkin() throws IOException {
        final URL url = new URL(URL_SKIN.replace("{uuid}", object.get("id").getAsString().replaceAll("-", "")));
        final HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);

        final BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        final StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println(response);
    }

    @Test
    @DisplayName("MojangAPI - New Methods")
    public void testNewMojangAPIMethods() throws IOException {
        final MojangAPI mojangAPI = new MojangAPI();
        final Optional<String> uuid = mojangAPI.getUUID(NAME);
        Assertions.assertTrue(uuid.isPresent());

        final Optional<MojangSkin> skin;
        try {
            skin = mojangAPI.getSkin(uuid.get());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        Assertions.assertTrue(skin.isPresent());
        Assertions.assertDoesNotThrow(this::testNewMojangAPIMethods);
    }
}
