package com.example.ghostswitch.custom_components;

import java.util.Random;

public class CustomEncryption {

    private String defaultKey;

    // Constructor
    public CustomEncryption(String defaultKey) {
        this.defaultKey = defaultKey;
    }

    // Generate PSK based on new rules
    private String generatePSK(String message) {
        Random random = new Random();
        int initialInsert = random.nextInt(3) + 1;      // 1-3 characters to insert initially
        int consecutiveCount = random.nextInt(2) + 1;   // 1-3 consecutive characters to insert

        StringBuilder psk = new StringBuilder();
        psk.append(initialInsert).append(consecutiveCount);

        // Append random digits for confusion
        String characters = "0123456789";
        for (int i = 0; i < 4; i++) {
            psk.append(characters.charAt(random.nextInt(characters.length())));
        }
        return psk.toString();  // Example: "136786"
    }

    // Encryption Function
    public String encrypt(String message) {
        StringBuilder encryptedMessage = new StringBuilder();
        int keyIndex = 0;

        // Generate PSK based on message
        String psk = generatePSK(message);
        int initialInsert = Character.getNumericValue(psk.charAt(0));
        int consecutiveCount = Character.getNumericValue(psk.charAt(1));

        // Insert initial characters from default key
        for (int i = 0; i < initialInsert && keyIndex < defaultKey.length(); i++) {
            encryptedMessage.append(defaultKey.charAt(keyIndex++));
        }

        // Spread default key characters based on consecutiveCount
        for (int i = 0; i < message.length(); i++) {
            encryptedMessage.append(message.charAt(i));

            // Insert consecutive characters from the default key
            for (int j = 0; j < consecutiveCount && keyIndex < defaultKey.length(); j++) {
                encryptedMessage.append(defaultKey.charAt(keyIndex++));
            }
        }

        // Append remaining default key characters if any
        while (keyIndex < defaultKey.length()) {
            encryptedMessage.append(defaultKey.charAt(keyIndex++));
        }

        // Append the PSK to the end of the encrypted message
        encryptedMessage.append(psk);
        return encryptedMessage.toString();
    }

    // 🛠 Decryption Function
    public String decrypt(String encryptedMessage) {
        // Extract PSK from the end (last 6 characters)
        String psk = encryptedMessage.substring(encryptedMessage.length() - 6);
        encryptedMessage = encryptedMessage.substring(0, encryptedMessage.length() - 6);

        // Extract initial insert and consecutive count from PSK
        int initialInsert = Character.getNumericValue(psk.charAt(0));
        int consecutiveCount = Character.getNumericValue(psk.charAt(1));

        StringBuilder decryptedMessage = new StringBuilder();

        // Skip initial inserted characters
        int index = initialInsert;

        // Remove inserted characters based on consecutiveCount
        while (index < encryptedMessage.length()) {
            decryptedMessage.append(encryptedMessage.charAt(index));
            index++;
            for (int j = 0; j < consecutiveCount && index < encryptedMessage.length(); j++) {
                index++;  // Skip consecutive characters
            }
        }

        // 🛠 Ensure extra characters are removed:
        // Check if decrypted message contains JSON-like structure
        String decryptedString = decryptedMessage.toString();
        int jsonStart = decryptedString.indexOf("{");
        int jsonEnd = decryptedString.lastIndexOf("}") + 1;

        if (jsonStart != -1 && jsonEnd != -1) {
            // Extract only the JSON part
            decryptedString = decryptedString.substring(jsonStart, jsonEnd);
        }

        return decryptedString;
    }

}
