package frc.trigon.robot.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class JsonHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Parses the given object to JSON and writes it to a JSON file,
     * using a safe way of writing.
     *
     * @param object the object to save
     * @param name   the name of the file to write to
     * @throws IOException if the method failed to write the object to the file
     */
    public static void parseToJsonAndWrite(String name, Object object) throws IOException {
        FilesHandler.safeWrite(FilesHandler.DEPLOY_PATH + name, parseObjectToJson(object));
    }

    /**
     * Parses a JSON file to an object.
     *
     * @param fileName the name of the file to read
     * @param type     the class to parse JSON to
     * @return the parsed JSON as the class
     */
    public static <T> T parseJsonFileToObject(String fileName, Class<T> type) {
        try {
            return GSON.fromJson(FilesHandler.readFile(FilesHandler.DEPLOY_PATH + fileName), type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Parses a JSON string to an object.
     *
     * @param jsonString the json string to parse
     * @param type       the class to parse JSON to
     * @return the parsed JSON as the class
     */
    public static <T> T parseJsonStringToObject(String jsonString, Class<T> type) {
        return GSON.fromJson(jsonString, type);
    }

    private static String parseObjectToJson(Object object) {
        return GSON.toJson(object);
    }
}
