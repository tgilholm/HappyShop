package ci553.happyshop.utility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests encryption/decryption methods
 * <p>
 * A test order is defined with the @Order annotation. This is because
 * the encryptString() and decryptString() tests are reliant on the getXORArray method.
 * <p>
 * The EncryptionHandler class has an overloaded method for the decrypt and encrypt methods.
 * The base method carries out the encryption, while the overload simply calls the base method
 * with the static cipher in the class.
 * <p>
 * The base methods are declared package-private (no modifier), meaning they are accessible in the
 * test class without unnecessarily exposing them.
 */
class EncryptionHandlerTest
{
    @Order(0)
    @Test
    @DisplayName("Test that bitwise XOR operations on the same plaintext are reversible on any cipher")
    void testGetXORArray()
    {
        // Test with a few different ciphers
        byte[] cipher0, cipher1, cipher2;
        byte[] result0, result1, result2;

        cipher0 = "1kGEJDkwkNKqPBd9jL0f4195oT8GQGyQ".getBytes(StandardCharsets.UTF_8);
        cipher1 = "XGZPT06kPjYiDhEVdEEJHDj3F5ngQKsF".getBytes(StandardCharsets.UTF_8);
        cipher2 = "wBCh6u85".getBytes(StandardCharsets.UTF_8);

        // Byte array to test-we should expect the same result when we reverse the XOR operation
        byte[] input = "ILOVEHAPPYSHOP".getBytes(StandardCharsets.UTF_8);

        // Perform the XOR once on each of the ciphers
        result0 = EncryptionHandler.getXORArray(input, cipher0);
        result1 = EncryptionHandler.getXORArray(input, cipher1);
        result2 = EncryptionHandler.getXORArray(input, cipher2);

        // Repeat the XOR and assert that we are back at the original array
        // The first parameter is the expected result (input), the second the actual.
        assertArrayEquals(input, EncryptionHandler.getXORArray(result0, cipher0));
        assertArrayEquals(input, EncryptionHandler.getXORArray(result1, cipher1));
        assertArrayEquals(input, EncryptionHandler.getXORArray(result2, cipher2));
    }


    @Test
    @DisplayName("Test that encrypting, then decrypting, gives the original, with the original cipher")
    void roundTripOnDefault()
    {
        String plaintext = "HAPPY SHOP";
        String encryptedString = EncryptionHandler.encryptString(plaintext);
        String decryptedString = EncryptionHandler.decryptString(encryptedString);  // Decrypt the new string

        // Test that the encryption algorithm worked
        assertNotEquals(plaintext, encryptedString);

        // If a successful round trip, the two will be equal.
        assertEquals(plaintext, decryptedString);
    }


    @Test
    @DisplayName("Test that encrypting, then decrypting, gives the original, with a custom cipher")
    void roundTripOnCustom()
    {
        String plaintext = "CI553-HAPPY SHOP";
        byte[] cipher = "POHS YPPAH-355IC".getBytes(StandardCharsets.UTF_8);    // Custom cipher (highly secure)

        String encryptedString = EncryptionHandler.encryptString(plaintext, cipher);
        String decryptedString = EncryptionHandler.decryptString(encryptedString, cipher);

        // Test that the encryption algorithm worked with the custom cipher
        assertNotEquals(plaintext, encryptedString);

        // If a successful round trip, the two will be equal.
        assertEquals(plaintext, decryptedString);
    }


    @Test
    @DisplayName("Test that encrypting with one cipher and attempting to decode with another will not result in the original")
    void wrongCipherDecode()
    {
        String plaintext = "HappyShop";
        byte[] cipher0 = "Pleased Shop".getBytes(StandardCharsets.UTF_8);    // First cipher
        byte[] cipher1 = "Distressed Shop".getBytes(StandardCharsets.UTF_8);    // Second cipher


        String encryptedString = EncryptionHandler.encryptString(plaintext, cipher0);

        // Test that the original cipher worked
        assertEquals(plaintext, EncryptionHandler.decryptString(encryptedString, cipher0));

        // Test that trying to decode the encrypted string with a different cipher doesn't work

        String wrongDecryptedString = EncryptionHandler.decryptString(encryptedString, cipher1); // "wrong" cipher
        assertNotEquals(plaintext, wrongDecryptedString);
    }
}