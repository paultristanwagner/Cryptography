package me.paultristanwagner.cryptography.aes;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * JavaDoc this file!
 * Created: 16.07.2019
 *
 * @author Paul Tristan Wagner <paultristanwagner@gmail.com>
 */
public class AESKeyGenerator {

    private AESKey aesKey;

    public void generate( SecureRandom secureRandom, int bits ) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance( "AES" );
            keyGen.init( bits, secureRandom );
            SecretKey secretKey = keyGen.generateKey();
            byte[] initVector = new byte[16];
            new Random().nextBytes( initVector );
            aesKey = new AESKey( secretKey.getEncoded(), initVector );
        } catch ( NoSuchAlgorithmException e ) {
            e.printStackTrace();
        }
    }

    public AESKey getAESKey() {
        if ( aesKey == null ) {
            throw new IllegalStateException( "Key pair has not been generated yet" );
        }
        return aesKey;
    }
}
