package ci553.happyshop.utility.handlers;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Final class defining encryption and decryption methods
 */
@SuppressWarnings("SameParameterValue")
public final class EncryptionHandler
{

    private EncryptionHandler()
    {
    }  // Final class, no instantiation


    // Randomly generated cipher; the string is converted to a byte array for encryption
    private static final byte[] CIPHER = "#7:7rX71Yf[zw]g16pIePufZGTHr<=Ml".getBytes(StandardCharsets.UTF_8);


    /**
     * Overloaded method. Allows users to simply encrypt strings without providing a cipher
     *
     * @param toEncrypt the <code>String</code> to encrypt
     * @return a Base64 encrypted <code>String</code>
     */
    public static @NotNull String encryptString(@NotNull String toEncrypt)
    {
        return encryptString(toEncrypt, CIPHER);
    }


    /**
     * Encrypts a <code>String</code> using XOR and Base64 (reversible)
     *
     * @param toEncrypt the <code>String</code> to encrypt
     * @param cipher    a byte array cipher
     * @return a Base64 encrypted <code>String</code>
     */
    static @NotNull String encryptString(@NotNull String toEncrypt, byte @NotNull [] cipher)
    {
        if (toEncrypt.isEmpty())
        {
            return "";
        }

        // Convert the input to a byte array
        byte[] plaintext = toEncrypt.getBytes(StandardCharsets.UTF_8);  // Same charset as cipher

        // Carry out the XOR on each of the bytes
        byte[] xorArray = getXORArray(plaintext, cipher);

        // Encodes the XOR-applied byte array in byte 64, then converts each byte to a character
        return Base64.getEncoder().encodeToString(xorArray);
    }


    /**
     * Overloaded method. Allows users to decrypt strings without knowing the cipher.
     *
     * @param toDecrypt the <code>String</code> to decrypt
     * @return a decrypted <code>String</code>
     */
    public static @NotNull String decryptString(@NotNull String toDecrypt)
    {
        return decryptString(toDecrypt, CIPHER);
    }


    /**
     * Decrypts a <code>String</code> by reversing the encryption algorithm
     *
     * @param toDecrypt the <code>String</code> to decrypt
     * @return a decrypted <code>String</code>
     */
    static @NotNull String decryptString(@NotNull String toDecrypt, byte @NotNull [] cipher)
    {
        if (toDecrypt.isEmpty())
        {
            return "";
        }
        // Convert the input to a byte array
        byte[] decoded = Base64.getDecoder().decode(toDecrypt);

        // Decode from Base64, repeat the XOR operation to reverse
        byte[] plaintext = getXORArray(decoded, cipher);

        // Encodes the XOR-applied byte array in byte 64
        return new String(plaintext, StandardCharsets.UTF_8);   // String allows specifying charset.
    }


    /**
     * <p>
     * This method performs a bitwise XOR (symbol is ^) on each of the bytes in the provided byte array.
     * The initial conversion to a byte array converts each of the symbols in the cipher to a binary sequence.
     * </p>
     * <p>
     * For example, the letters "a" and "b" when encoded in UTF-8 are:
     * a = 01100001
     * b = 01100010
     * <p>
     * Bitwise XOR compares each digit and returns 1 if the two digits are the same. Otherwise, 0 is returned.
     * If "a" is taken to be the plaintext, and "b" the cipher, the result is:
     * <br>
     * 01100001 ^ 01100010 = 00000011
     * <br>
     * <br>
     * i % keyLength returns the remainder of the division between the former and latter, where
     * "i" is the current index being converted, and keyLength is the length of the cipher.
     * <p>
     * This operation is reversible. Repeating the same operation with 00000011 as the plaintext and 01100010 the cipher,
     * we get:
     * <br>
     * 00000011 ^ 01100010 = 01100001
     * <p>
     * This is equal to "a".
     *
     * @param plaintext the byte array on which to perform the operation
     * @param cipher a byte array to perform a bitwise XOR with the plaintext
     * @return a byte array, on which each of the bytes has been XOR'ed with the cipher
     */
    @Contract(pure = true)
    static byte @NotNull [] getXORArray(byte @NotNull [] plaintext, byte @NotNull [] cipher)
    {
        // Output byte array, same length as input
        byte[] output = new byte[plaintext.length];
        int keyLength = cipher.length;

        for (int i = 0; i < plaintext.length; i++)
        {

            output[i] = (byte) (plaintext[i] ^ cipher[i % keyLength]);
        }
        return output;  // The encoded array
    }
}
