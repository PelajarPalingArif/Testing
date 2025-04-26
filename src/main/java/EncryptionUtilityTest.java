import com.astar.common.library.utils.EncryptionUtility;

import javax.crypto.*;

public class EncryptionUtilityTest {
    public static void main(String[] args) throws Exception {
        EncryptionUtility.initializeEncryption();

        String plainText = "Biawak Masak Kicap ASDASDAXSMCASINANSNFAS ASFA ASFASDAS A";
        String password = "arif";
        String salt = EncryptionUtility.generateSalt();

        System.out.println("salt : " + salt);

        SecretKey secretKey = EncryptionUtility.AESGetKeyFromPassword(password, salt);
        String cipherText = EncryptionUtility.AESEncrypt(plainText, secretKey);
        String decryptedCipherText= EncryptionUtility.AESDecrypt(cipherText, password, salt);
        System.out.println("Encrypted Text : " + cipherText);
        System.out.println("Decrypted Text : " + decryptedCipherText);
    }
}
