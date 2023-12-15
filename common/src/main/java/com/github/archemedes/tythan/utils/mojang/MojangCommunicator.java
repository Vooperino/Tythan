package com.github.archemedes.tythan.utils.mojang;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass

public class MojangCommunicator {
    public static class AuthenticationException extends Exception {
        private static final long serialVersionUID = -2260469046718388024L;

        public AuthenticationException(String msg) {
            super(msg);
        }
    }

    public static AuthenthicationData authenthicate(MinecraftAccount account) throws IOException, AuthenticationException {
        //See wiki.vg/Authenthication
        InputStream in = null;
        BufferedWriter out = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL("https://authserver.mojang.com/authenticate");
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true); //lets us write method body
            conn.setDoInput(true); //cuz fuck it why not

            conn.setRequestMethod("POST");
            conn.setRequestProperty( "Content-Type", "application/json" ); //required by Mojang

            JsonObject payload = new JsonObject();
            JsonObject agent = new JsonObject();
            agent.addProperty("name", "Minecraft");
            agent.addProperty("version", "1");
            payload.add("agent", agent);
            payload.addProperty("username", account.username);
            payload.addProperty("password", account.password);

            String payloadString = payload.toString();

            out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            out.write(payloadString);
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode == 200) {
                in = new BufferedInputStream(conn.getInputStream());
                String result = new String(ByteStreams.toByteArray(in), Charsets.UTF_8);
                JsonParser parser = new JsonParser();
                JsonObject responseJson = parser.parse(result).getAsJsonObject();

                AuthenthicationData data = new AuthenthicationData();
                data.accessToken = responseJson.get("accessToken").getAsString();
                data.uuid = responseJson.get("selectedProfile").getAsJsonObject().get("id").getAsString();
                return data;

            } else if(responseCode == 403){
                throw new AuthenticationException("Mojang returned 403 on authenthication."
                        + " Account: " + account.username + " may have had its password changed!");
            } else {
                throw new RuntimeException("Unexpected response code on authenthication: " + responseCode);
            }

        } finally {
            if(in != null) in.close();
            if(out != null) out.close();
            if(conn != null) conn.disconnect();
        }
    }

    public static void setSkin(AuthenthicationData data, String skinUrl) throws IOException {
        //See wiki.vg/Mojang_API#Change_Skin
        InputStream in = null;
        DataOutputStream out = null;
        HttpURLConnection conn = null;
        try {
            String urlString = "https://api.mojang.com/user/profile/"+ data.uuid +"/skin";
            URL url = new URL(urlString);

            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true); //lets us write method body
            conn.setDoInput(true); //cuz fuck it why not

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + data.accessToken); //shows we're logged in

            String query = "model="+ URLEncoder.encode("slim","UTF-8");
            query += "&";
            query += "url="+ URLEncoder.encode(skinUrl,"UTF-8") ;

            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(query);
        } finally {
            if(in != null) in.close();
            if(out != null) out.close();
            if(conn != null) conn.disconnect();
        }
    }

    public static JsonObject requestSkin(UUID uuid) throws IOException {
        String uuidUser = uuid.toString().replace("-", "");
        return requestSkin(uuidUser);
    }

    @Nullable @SneakyThrows
    public static SkinData requestSkinData(String uuidUser) {
        InputStreamReader in = null;
        HttpURLConnection con = null;

        @Nullable SkinData data = null;

        try {//Request player profile from mojang api
            URL url;
            url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidUser + "?unsigned=false");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);
            in = new InputStreamReader(con.getInputStream());

            JsonParser parser = new JsonParser();
            JsonObject result = parser.parse(in).getAsJsonObject();
            JsonArray properties = result.get("properties").getAsJsonArray();
            JsonObject textures = properties.get(0).getAsJsonObject();

            String name = textures.get("name").getAsString();

            //Validation that these exist
            var v = textures.get("value").getAsString();
            var t =textures.get("signature").getAsString();

            Validate.isTrue("textures".equals(name), "Skin properties file fetched from Mojang had wrong name: " + name);
            data = new SkinData() {
                @Override public @NotNull @Unmodifiable String getValue() {return v;}
                @Override public @NotNull @Unmodifiable String getSignature() {return t;}
                @Override public @NotNull @Unmodifiable boolean isSlim() {
                    return false;
                }
            };
        } finally {
            if (in != null) in.close();
            if (con != null) con.disconnect();
        }
        return data;
    }


    public static JsonObject requestSkin(String uuidUser) throws IOException {
        InputStreamReader in = null;
        HttpURLConnection con = null;

        try {//Request player profile from mojang api
            URL url;
            url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuidUser + "?unsigned=false");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);
            in = new InputStreamReader(con.getInputStream());

            JsonParser parser = new JsonParser();
            JsonObject result = parser.parse(in).getAsJsonObject();
            JsonArray properties = result.get("properties").getAsJsonArray();
            JsonObject textures = properties.get(0).getAsJsonObject();

            String name = textures.get("name").getAsString();

            //Validation that these exist
            textures.get("value").getAsString();
            textures.get("signature").getAsString();

            Validate.isTrue("textures".equals(name), "Skin properties file fetched from Mojang had wrong name: " + name);
            return textures;

        } finally {
            if (in != null) in.close();
            if (con != null) con.disconnect();
        }
    }

    public static List<String> requestAllUsernames(UUID uuid) throws IOException {
        List<String> result = new ArrayList<>();
        JsonArray names = requestUsernamesJson(uuid);
        for(JsonElement n : names) {
            JsonObject name = n.getAsJsonObject();
            result.add(String.valueOf(name.get("name")));
        }

        return result;
    }

    public static String requestCurrentUsername(UUID uuid) throws IOException {
        JsonArray names = requestUsernamesJson(uuid);
        JsonObject firstName = names.get(names.size() - 1).getAsJsonObject();
        return String.valueOf(firstName.get("name"));
    }

    private static JsonArray requestUsernamesJson(UUID uuid) throws IOException {
        String uuid_string = uuid.toString().replaceAll("-", "");

        InputStreamReader in = null;
        HttpURLConnection con = null;

        try {//Request player profile from mojang api
            URL url;
            url = new URL("https://api.mojang.com/user/profiles/"+uuid_string+"/names");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            in = new InputStreamReader(con.getInputStream());

            JsonParser parser = new JsonParser();
            JsonArray result = parser.parse(in).getAsJsonArray();
            return result;
        } finally {
            if (in != null) in.close();
            if (con != null) con.disconnect();
        }
    }

    public static UUID requestPlayerUUID(String name) throws IOException {
        return requestPlayerUUIDs(new String[]{name} )[0];
    }

    public static UUID[] requestPlayerUUIDs(String... names) throws IOException {
        if(names.length > 100) throw new ArrayIndexOutOfBoundsException("Can only request 100 uuids at a time");
        if(names.length > Arrays.stream(names)
                .filter(Objects::nonNull)
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList()).size())
            throw new IllegalArgumentException("Needs an array of unique, non-null names");

        InputStreamReader in = null;
        BufferedWriter out = null;
        HttpURLConnection con = null;

        UUID[] uuids = new UUID[names.length];

        try {//Request player profile from mojang api
            URL url = new URL("https://api.mojang.com/profiles/minecraft");
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestMethod("POST");
            con.setRequestProperty( "Content-Type", "application/json" );

            var array = new JsonArray();
            for(var name:names) array.add(name);
            String payload = array.toString();

            out = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            out.write(payload);
            out.close();

            in = new InputStreamReader(con.getInputStream());

            JsonParser parser = new JsonParser();
            JsonArray result = (JsonArray) parser.parse(in);
            int i = 0;
            for(Object x : result) {
                JsonObject o = (JsonObject) x;
                String name = o.get("name").getAsString();
                do {
                    while(uuids[i] != null) i++;

                    if(name.equalsIgnoreCase(names[i])) {
                        String id = o.get("id").getAsString();
                        String uid = id.substring(0, 8) + '-' + id.substring(8,12) + '-'
                                + id.substring(12,16) + '-' + id.substring(16, 20)
                                + '-' + id.substring(20);
                        uuids[i++] = UUID.fromString(uid);
                        break;
                    }
                }while(++i < names.length);

            }
            return uuids;
/*		}catch(ParseException e) {
			throw new RuntimeException();*/
        }finally {
            if (in != null) in.close();
            if (out != null) out.close();
            if (con != null) con.disconnect();
        }

    }

    public static class MinecraftAccount{ public String username,password;}

    public static class AuthenthicationData{ public String accessToken,uuid;}

}
