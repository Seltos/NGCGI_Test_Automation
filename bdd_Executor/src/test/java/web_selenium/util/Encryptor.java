package web_selenium.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

public class Encryptor {

    Cipher cipher;

    String initVector;

    SecretKeySpec key;

    String password;

    String salt;

    public Encryptor(String password) {
        this.initVector = "0123456789ABCDEF";
        this.password = password;
        this.salt = "NO_SALT";
    }

    public String decrypt(String encryptedData) {
        try {
            IvParameterSpec iv = new IvParameterSpec(this.initVector.getBytes("UTF-8"));

            SecretKey key = getKey();

            this.getCypher().init(Cipher.DECRYPT_MODE, key, iv);

            byte[] original = this.getCypher().doFinal(Base64.getDecoder().decode(encryptedData.toString()));

            return new String(original);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String encrypt(String data) {
        try {
            IvParameterSpec iv = new IvParameterSpec(this.initVector.getBytes("UTF-8"));

            SecretKey key = getKey();
            this.getCypher().init(Cipher.ENCRYPT_MODE, key, iv);

            byte[] encryptedBytes = this.getCypher().doFinal(data.getBytes());

            return new String(Base64.getEncoder().encode(encryptedBytes));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Cipher getCypher() throws Exception {
        if (this.cipher == null) {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        }

        return this.cipher;
    }

    private SecretKeySpec getKey() throws Exception {
        if (this.key == null) {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), this.salt.getBytes("UTF-8"), 5, 256);
            SecretKey secretKey = factory.generateSecret(spec);

            this.key = new SecretKeySpec(Arrays.copyOf(secretKey.getEncoded(), 16), "AES");
        }

        return this.key;
    }
}
