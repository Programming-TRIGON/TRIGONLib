package org.trigon.utilities;

/**
 * A class that contains utility methods for strings.
 */
public class StringUtilities {
    /**
     * Converts a string to camel case.
     *
     * @param input the string to convert
     * @return the string in camel case
     */
    public static String toCamelCase(String input) {
        String[] parts = input.split("_");
        StringBuilder camelCase = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].toLowerCase();
            if (i == 0)
                camelCase.append(part);
            else
                camelCase.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }

        return camelCase.toString();
    }
}