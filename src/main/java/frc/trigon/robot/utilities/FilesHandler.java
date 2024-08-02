package frc.trigon.robot.utilities;

import edu.wpi.first.wpilibj.Filesystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A class that handles writing, deleting, and renaming files.
 * This class also holds the "DEPLOY_PATH" constant, which is the absolute path of the deploy folder.
 */
public class FilesHandler {
    public static final String DEPLOY_PATH = Filesystem.getDeployDirectory().getPath() + "/";

    /**
     * Deletes the given file.
     *
     * @param absolutePath the file's absolute path
     * @throws IOException if the method failed to delete the specified file
     */
    public static void deleteFile(String absolutePath) throws IOException {
        final File file = new File(absolutePath);
        if (!file.delete())
            throw new IOException("Failed to delete the file \"" + absolutePath + "\".");
    }

    /**
     * Writes the given string to a file.
     *
     * @param absolutePath the file's absolute path
     * @param str          the string to write to the file
     * @throws IOException if the method failed to write the string to the file
     */
    public static void writeStringToFile(String absolutePath, String str) throws IOException {
        final FileWriter fileWriter = new FileWriter(absolutePath);
        fileWriter.write(str);
        fileWriter.close();
    }

    /**
     * Renames a file.
     *
     * @param absolutePath the file's absolute path
     * @param newName      the new desired name
     * @throws IOException if the method failed to rename the file
     */
    public static void renameFile(String absolutePath, String newName) throws IOException {
        final File file = new File(absolutePath);
        final String newAbsolutePath = extractPathFromAbsolutePath(absolutePath) + newName;
        if (!file.renameTo(new File(newAbsolutePath)))
            throw new IOException("Failed to rename file " + absolutePath + " to " + newName);
    }

    /**
     * Creates a file using a safe method of writing.
     * This method will write the string to a temporary file,
     * delete the original file,
     * and rename the temporary file to the desired name.
     *
     * @param absolutePath the file's absolute path
     * @param str          the string to write to the file
     * @throws IOException if the method failed to safe write the file
     */
    public static void safeWrite(String absolutePath, String str) throws IOException {
        final String fileName = extractFileNameFromAbsolutePath(absolutePath);

        writeStringToFile(absolutePath + ".tmp", str);
        deleteFile(absolutePath);
        renameFile(absolutePath + ".tmp", fileName);
    }

    /**
     * Reads a file and returns its content as a string.
     * If the file does not exist, it will check for a .tmp file.
     *
     * @param absolutePath the file's absolute path
     * @return the file contents
     * @throws IOException if the method failed to read the file
     */
    public static String readFile(String absolutePath) throws IOException {
        final String newPath = !fileExists(absolutePath) && fileExists(absolutePath + ".tmp") ?
                absolutePath + ".tmp" :
                absolutePath;
        return Files.readString(Path.of(newPath));
    }

    private static boolean fileExists(String absolutePath) {
        return new File(absolutePath).exists();
    }

    private static String extractPathFromAbsolutePath(String absolutePath) {
        if (!absolutePath.contains("/"))
            return absolutePath + "/";

        int lastSlashIndex = absolutePath.lastIndexOf("/");
        if (absolutePath.endsWith("/"))
            lastSlashIndex = absolutePath.lastIndexOf("/", lastSlashIndex - 1);

        return absolutePath.substring(0, lastSlashIndex + 1);
    }

    private static String extractFileNameFromAbsolutePath(String absolutePath) {
        int slashIndex = absolutePath.lastIndexOf("/");

        if (absolutePath.endsWith("/"))
            slashIndex = absolutePath.lastIndexOf("/", slashIndex - 1);

        return absolutePath.substring(slashIndex + 1).replace("/", "");
    }
}
