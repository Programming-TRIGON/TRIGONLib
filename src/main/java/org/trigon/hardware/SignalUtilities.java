package org.trigon.hardware;

/**
 * A class that contains utility methods for signals.
 */
public class SignalUtilities {
    /**
     * Converts an enum name to a signal name.
     *
     * @param enumName The enum name
     * @return The signal name
     */
    public static String enumNameToSignalName(String enumName) {
        final String lowercaseName = enumName.toLowerCase();
        final String nameNoUnderscore = lowercaseName.replace("_", "");
        final char[] camelCaseNameChars = new char[nameNoUnderscore.length()];

        boolean wasLastUnderscore = false;
        camelCaseNameChars[0] = Character.toUpperCase(lowercaseName.charAt(0));
        int lastIndex = 1;
        for (int i = 1; i < lowercaseName.length(); i++) {
            final char currentChar = lowercaseName.charAt(i);

            if (currentChar == '_') {
                wasLastUnderscore = true;
                continue;
            }

            if (wasLastUnderscore) {
                wasLastUnderscore = false;
                camelCaseNameChars[lastIndex] = Character.toUpperCase(currentChar);
            } else {
                camelCaseNameChars[lastIndex] = currentChar;
            }

            lastIndex++;
        }

        return new String(camelCaseNameChars);
    }
}
