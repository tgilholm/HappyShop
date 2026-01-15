package ci553.happyshop.utility;

import org.jetbrains.annotations.NotNull;

public final class EncryptionHandler
{
    private EncryptionHandler()
    {
    }  // Final class, no instantiation


    /**
     * Encrypts a <code>String</code> using the encryption algorithm
     * @param toEncrypt the <code>String</code> to encrypt
     * @return an encrypted <code>String</code>
     */
    public static String encryptString(@NotNull String toEncrypt)
    {
        // todo implement encryption
        return toEncrypt;
    }

    /**
     * Decrypts a <code>String</code> by reversing the encryption algorithm
     * @param toDecrypt the <code>String</code> to decrypt
     * @return a decrypted <code>String</code>
     */
    public static String decryptString(@NotNull String toDecrypt)
    {
        // todo implement decryption
        return toDecrypt;
    }
}
