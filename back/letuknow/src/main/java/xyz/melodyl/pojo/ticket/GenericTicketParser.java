package xyz.melodyl.pojo.ticket;

import org.apache.commons.codec.binary.Base64;
import xyz.melodyl.pojo.ticket.TicketParser;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

public class GenericTicketParser implements TicketParser {

    private static final String KEY_ALGORITHM = "AES";
    private static final int AES_ALGORITHM_LENGTH = 128;
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private SecretKeySpec secretKeySpec;
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    public GenericTicketParser(String password) {
        secretKeySpec = createSecretKey(Objects.requireNonNull(password));
        encryptCipher = createEncryptCipher();
        decryptCipher = createDecryptCipher();
    }

    @Override
    public String encryptContentToTicket(String content) throws Exception {
        byte[] result = encryptCipher.doFinal(content.getBytes(TICKET_DEFAULT_CHARSET));// 加密
        return Base64.encodeBase64String(result);
    }

    @Override
    public String decryptTicketToContent(String ticket) throws Exception {
        byte[] result = decryptCipher.doFinal(Base64.decodeBase64(ticket));
        return new String(result, TICKET_DEFAULT_CHARSET);
    }

    //-D java.security.egd=file:/dev/./urandom
    // to improve the speed of random generation
    private SecretKeySpec createSecretKey(final String password){
        SecretKeySpec secretKeySpec = null;

        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes(TICKET_DEFAULT_CHARSET));

            KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            kg.init(AES_ALGORITHM_LENGTH, random);

            SecretKey secretKey = kg.generateKey();
            secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Objects.requireNonNull(secretKeySpec);
    }

    private Cipher createEncryptCipher() {
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);//设置为加密模式
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        return Objects.requireNonNull(cipher);
    }

    private Cipher createDecryptCipher() {
        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);//设置为解密模式
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        return Objects.requireNonNull(cipher);
    }
}
